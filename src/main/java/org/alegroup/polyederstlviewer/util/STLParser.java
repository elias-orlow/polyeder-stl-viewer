package org.alegroup.polyederstlviewer.util;

import org.alegroup.polyederstlviewer.constants.ErrorMessages;
import org.alegroup.polyederstlviewer.constants.GeneralConstants;
import org.alegroup.polyederstlviewer.constants.STLParserConstants;
import org.alegroup.polyederstlviewer.model.geometry.analysis.AreaCalculator;
import org.alegroup.polyederstlviewer.model.geometry.analysis.AreaResult;
import org.alegroup.polyederstlviewer.model.geometry.analysis.STLParseResult;
import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Utility class responsible for parsing STL files in both ASCII and binary formats.
 * The parser extracts triangle geometry, computes surface area contributions,
 * and constructs a {@link Polyhedron} instance containing all parsed triangles.
 */
public class STLParser
{

    /**
     * Parses an STL file (ASCII or binary) and returns a {@link STLParseResult}
     * containing the polyhedron and its computed area results.
     *
     * @param file the STL file to parse
     * @return a result object containing the parsed polyhedron and area analysis
     * @throws RuntimeException if the file cannot be parsed or read
     * @precondition file != null AND file must exist and be readable
     * @postcondition A valid {@link STLParseResult} is returned OR an exception is thrown
     */
    public static STLParseResult parse (File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException(STLParserConstants.FILE_NULL);
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)))
        {

            bis.mark(STLParserConstants.MARK_LIMIT);
            byte[] headerBytes = new byte[STLParserConstants.HEADER_READ_SIZE];
            int read = bis.read(headerBytes);
            bis.reset();

            String headerSnippet = GeneralConstants.EMPTY_STRING;
            if (read > GeneralConstants.INT_ZERO)
            {
                headerSnippet = new String(headerBytes, GeneralConstants.INT_ZERO, read, STLParserConstants.ASCII_CHARSET)
                        .toLowerCase(Locale.ROOT);
            }

            if (looksLikeAscii(headerSnippet))
            {
                return parseAscii(file);
            }
            else
            {
                return parseBinary(file);
            }

        } catch (STLFormatException | IOException e)
        {
            throw new RuntimeException(ErrorMessages.NO_STL_FILE);
        }
    }

    /**
     * Determines whether the given text snippet suggests an ASCII STL file.
     *
     * @param snippet a text snippet from the beginning of the file
     * @return true if the snippet resembles ASCII STL structure, false otherwise
     * @precondition snippet != null
     * @postcondition Boolean result indicates ASCII likelihood
     */
    private static boolean looksLikeAscii (String snippet)
    {
        if (snippet.contains(STLParserConstants.TOKEN_FACET)
                || snippet.contains(STLParserConstants.TOKEN_VERTEX)
                || snippet.contains(STLParserConstants.TOKEN_OUTER_LOOP))
        {
            return true;
        }
        if (snippet.trim().startsWith(STLParserConstants.TOKEN_SOLID)
                && snippet.contains(STLParserConstants.NEWLINE))
        {
            return true;
        }
        return false;
    }

    /**
     * Parses an ASCII STL file and constructs a {@link STLParseResult}.
     *
     * @param file the ASCII STL file
     * @return a result object containing the parsed polyhedron and area analysis
     * @throws IOException        if reading fails
     * @throws STLFormatException if the file is malformed
     * @precondition file != null AND file must contain valid ASCII STL syntax
     * @postcondition All triangles are parsed and area contributions computed
     */
    private static STLParseResult parseAscii (File file)
            throws IOException, STLFormatException
    {

        List<Triangle> triangleList = new ArrayList<>();
        AreaCalculator areaCalculator = new AreaCalculator();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {

            String line;
            Vector3D normal = null;
            Vertex[] currentVertices = new Vertex[STLParserConstants.VERTEX_COUNT];
            int vertexIndex = GeneralConstants.FIRST_INDEX;

            while ((line = br.readLine()) != null)
            {

                line = line.trim();
                if (line.isEmpty())
                {
                    continue;
                }

                String lower = line.toLowerCase(Locale.ROOT);

                if (lower.startsWith(STLParserConstants.TOKEN_FACET_NORMAL))
                {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();
                    st.nextToken();

                    float nx = Float.parseFloat(st.nextToken());
                    float ny = Float.parseFloat(st.nextToken());
                    float nz = Float.parseFloat(st.nextToken());
                    normal = new Vector3D(nx, ny, nz);
                }

                if (lower.startsWith(STLParserConstants.TOKEN_VERTEX))
                {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken();

                    float x = Float.parseFloat(st.nextToken());
                    float y = Float.parseFloat(st.nextToken());
                    float z = Float.parseFloat(st.nextToken());

                    currentVertices[vertexIndex++] = new Vertex(x, y, z);
                }

                if (lower.startsWith(STLParserConstants.TOKEN_ENDFACET))
                {

                    if (vertexIndex != STLParserConstants.VERTEX_COUNT)
                    {
                        throw new STLFormatException(STLParserConstants.ERROR_INCOMPLETE_FACET);
                    }

                    List<Edge> edges = new ArrayList<>();
                    edges.add(new Edge(currentVertices[GeneralConstants.FIRST_INDEX], currentVertices[GeneralConstants.SECOND_INDEX]));
                    edges.add(new Edge(currentVertices[GeneralConstants.SECOND_INDEX], currentVertices[GeneralConstants.THIRD_INDEX]));
                    edges.add(new Edge(currentVertices[GeneralConstants.THIRD_INDEX], currentVertices[GeneralConstants.FIRST_INDEX]));

                    Triangle triangle = new Triangle(edges, normal);

                    triangleList.add(triangle);
                    areaCalculator.addTriangle(triangle);

                    vertexIndex = GeneralConstants.FIRST_INDEX;
                }
            }
        }

        AreaResult areaResult = areaCalculator.finish();
        return new STLParseResult(new Polyhedron(triangleList), areaResult);
    }

    /**
     * Parses a binary STL file and constructs a {@link STLParseResult}.
     *
     * @param file the binary STL file
     * @return a result object containing the parsed polyhedron and area analysis
     * @throws IOException        if reading fails
     * @throws STLFormatException if the file is malformed
     * @precondition file != null AND file must contain valid binary STL structure
     * @postcondition All triangles are parsed and area contributions computed
     */
    private static STLParseResult parseBinary (File file)
            throws IOException, STLFormatException
    {

        byte[] all = java.nio.file.Files.readAllBytes(file.toPath());
        AreaCalculator areaCalculator = new AreaCalculator();

        if (all.length < STLParserConstants.MIN_BINARY_SIZE)
        {
            throw new STLFormatException(STLParserConstants.ERROR_BINARY_TOO_SHORT);
        }

        ByteBuffer bb = ByteBuffer.wrap(all);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.position(STLParserConstants.BINARY_HEADER_SIZE);

        long unsignedCount = Integer.toUnsignedLong(bb.getInt());

        List<Triangle> triangleList = new ArrayList<>();

        for (long i = GeneralConstants.INT_ZERO; i < unsignedCount; i++)
        {

            if (bb.remaining() < STLParserConstants.BINARY_TRIANGLE_SIZE)
            {
                break;
            }

            float nx = bb.getFloat();
            float ny = bb.getFloat();
            float nz = bb.getFloat();

            float v0x = bb.getFloat();
            float v0y = bb.getFloat();
            float v0z = bb.getFloat();

            float v1x = bb.getFloat();
            float v1y = bb.getFloat();
            float v1z = bb.getFloat();

            float v2x = bb.getFloat();
            float v2y = bb.getFloat();
            float v2z = bb.getFloat();

            bb.getShort();

            Vector3D normal = new Vector3D(nx, ny, nz);

            Vertex a = new Vertex(v0x, v0y, v0z);
            Vertex b = new Vertex(v1x, v1y, v1z);
            Vertex c = new Vertex(v2x, v2y, v2z);

            List<Edge> edgeList = new ArrayList<>();
            edgeList.add(new Edge(a, b));
            edgeList.add(new Edge(b, c));
            edgeList.add(new Edge(c, a));

            Triangle triangle = new Triangle(edgeList, normal);

            triangleList.add(triangle);
            areaCalculator.addTriangle(triangle);
        }

        AreaResult areaResult = areaCalculator.finish();
        Polyhedron poly = new Polyhedron(triangleList);

        return new STLParseResult(poly, areaResult);
    }
}