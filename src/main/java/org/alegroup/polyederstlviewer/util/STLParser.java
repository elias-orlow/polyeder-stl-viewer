package org.alegroup.polyederstlviewer.util;

import org.alegroup.polyederstlviewer.model.geometry.mesh.Polyhedron;
import org.alegroup.polyederstlviewer.model.geometry.polygon.Triangle;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Edge;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vertex;
import org.alegroup.polyederstlviewer.model.geometry.primitive.Vector3D;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Parser for STL files supporting ASCII and binary formats.
 *
 * @precondition File exists and is readable.
 * @postcondition Returns a Polyhedron built from triangles in the file.
 */
public class STLParser
{
    /**
     * Parse an STL file (ASCII or binary) and return a Polyhedron.
     *
     * @precondition file != null and file exists.
     * @postcondition Returns Polyhedron or throws STLFormatException on error.
     */
    public static Polyhedron parse(File file)
            throws IOException, STLFormatException
    {
        if (file == null)
        {
            throw new IllegalArgumentException("file must not be null");
        }

        // Heuristic: read first 256 bytes as text and look for ASCII tokens
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file)))
        {
            bis.mark(1024);
            byte[] headerBytes = new byte[256];
            int read = bis.read(headerBytes);
            bis.reset();

            String headerSnippet = "";
            if (read > 0)
            {
                headerSnippet = new String(headerBytes, 0, read, "US-ASCII").toLowerCase(Locale.ROOT);
            }

            if (looksLikeAscii(headerSnippet))
            {
                return parseAscii(file);
            }
            else
            {
                return parseBinary(file);
            }
        }
    }

    private static boolean looksLikeAscii(String snippet)
    {
        if (snippet.contains("facet") || snippet.contains("vertex") || snippet.contains("outer loop"))
        {
            return true;
        }
        // If starts with "solid" and contains newline, likely ASCII
        if (snippet.trim().startsWith("solid") && snippet.contains("\n"))
        {
            return true;
        }
        return false;
    }

    /**
     * Parse ASCII STL.
     *
     * @precondition file is ASCII STL.
     * @postcondition Returns Polyhedron.
     */
    private static Polyhedron parseAscii(File file)
            throws IOException, STLFormatException
    {
        List<Triangle> triangleList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String line;
            Vector3D normal = null;
            Vertex[] currentVertices = new Vertex[3];
            int vertexIndex = 0;

            while ((line = br.readLine()) != null)
            {
                line = line.trim();
                if (line.isEmpty())
                {
                    continue;
                }

                String lower = line.toLowerCase(Locale.ROOT);

                if (lower.startsWith("facet normal"))
                {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken(); // facet
                    st.nextToken(); // normal

                    float nx = Float.parseFloat(st.nextToken());
                    float ny = Float.parseFloat(st.nextToken());
                    float nz = Float.parseFloat(st.nextToken());
                    normal = new Vector3D(nx, ny, nz);

                    vertexIndex = 0;
                    continue;
                }

                if (lower.startsWith("outer loop"))
                {
                    continue;
                }

                if (lower.startsWith("vertex"))
                {
                    StringTokenizer st = new StringTokenizer(line);
                    st.nextToken(); // vertex

                    float x = Float.parseFloat(st.nextToken());
                    float y = Float.parseFloat(st.nextToken());
                    float z = Float.parseFloat(st.nextToken());

                    currentVertices[vertexIndex++] = new Vertex(x, y, z);
                    continue;
                }

                if (lower.startsWith("endfacet"))
                {
                    if (vertexIndex != 3)
                    {
                        throw new STLFormatException("Facet ended without 3 vertices");
                    }

                    List<Edge> edges = new ArrayList<>();
                    edges.add(new Edge(currentVertices[0], currentVertices[1]));
                    edges.add(new Edge(currentVertices[1], currentVertices[2]));
                    edges.add(new Edge(currentVertices[2], currentVertices[0]));

                    triangleList.add(new Triangle(edges, normal));
                }
            }
        }
        // todo entfernen
        for (Triangle t: triangleList)
        {
            System.out.println(t.toString());
        }
        return new Polyhedron(triangleList);
    }

//    private static Polyhedron parseAscii(File file)
//            throws IOException, STLFormatException
//    {
//        Polyhedron poly = null;
//
//        try (BufferedReader br = new BufferedReader(new FileReader(file)))
//        {
//            String line;
//            Vector3D normal = null;
//            List<Edge> edgeList = new ArrayList<>();
//            Vertex[] currentVertices = new Vertex[3];
//            int vertexIndex = 0;
//            List<Triangle> triangleList = new ArrayList<>();
//            boolean inFacet = false;
//
//            while ((line = br.readLine()) != null)
//            {
//                line = line.trim();
//                if (line.isEmpty())
//                {
//                    continue;
//                }
//
//                String lower = line.toLowerCase(Locale.ROOT);
//                if (lower.startsWith("facet normal"))
//                {
//                    inFacet = true;
//                    vertexIndex = 0;
//                    StringTokenizer st = new StringTokenizer(line);
//                    st.nextToken(); // skip 'facet'
//                    st.nextToken(); // skip 'normal'
//                    if (st.countTokens() < 3)
//                    {
//                        throw new STLFormatException("Normal line does not contain 3 coordinates: " + line);
//                    }
//                    float x = Float.parseFloat(st.nextToken());
//                    float y = Float.parseFloat(st.nextToken());
//                    float z = Float.parseFloat(st.nextToken());
//                    normal = new Vector3D(x, y, z);
//
//                    line = br.readLine(); // skip 'outer loop' line
//
//                    for (int i = 0; i < 3; i++)
//                    {
//                        line = br.readLine().trim(); // a 'vertex' line
//                        if (line.isEmpty())
//                        {
//                            continue;
//                        }
//
//                        lower = line.toLowerCase(Locale.ROOT);
//                        if (lower.startsWith("vertex"))
//                        {
//                            StringTokenizer st2 = new StringTokenizer(line);
//                            st2.nextToken(); // skip 'vertex'
//                            if (st2.countTokens() < 3)
//                            {
//                                throw new STLFormatException("Vertex line does not contain 3 coordinates: " + line);
//                            }
//                            float u = Float.parseFloat(st2.nextToken());
//                            float v = Float.parseFloat(st2.nextToken());
//                            float w = Float.parseFloat(st2.nextToken());
//                            currentVertices[vertexIndex++] = new Vertex(u, v, w);
//                            continue;
//                        }
//                        else
//                        {
//                            throw new STLFormatException("Vertex line expected but not found: " + line);
//                        }
//                    }
//                    edgeList.add(new Edge(currentVertices[0], currentVertices[1]));
//                    edgeList.add(new Edge(currentVertices[1], currentVertices[2]));
//                    edgeList.add(new Edge(currentVertices[2], currentVertices[0]));
//                }
//                triangleList.add(new Triangle(edgeList, normal));
//                edgeList.clear();
//            }
//            poly = new Polyhedron(triangleList);
//        }
//        return poly;
//    }

    /**
     * Parse binary STL.
     *
     * @precondition file is binary STL.
     * @postcondition Returns Polyhedron.
     */
    private static Polyhedron parseBinary(File file)
            throws IOException, STLFormatException
    {
        byte[] all = java.nio.file.Files.readAllBytes(file.toPath());
        if (all.length < 84)
        {
            throw new STLFormatException("Binary STL too short");
        }

        // header 80 bytes ignored
        ByteBuffer bb = ByteBuffer.wrap(all);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.position(80);
        long unsignedCount = Integer.toUnsignedLong(bb.getInt());
        long expectedSize = 80L + 4L + unsignedCount * 50L;

        Polyhedron poly = null;
        List<Triangle> triangleList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        for (long i = 0; i < unsignedCount; i++)
        {
            if (bb.remaining() < 50)
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

            // attribute short
            bb.getShort();

            Vector3D normal = new Vector3D(nx, ny, nz);
            Vertex a = new Vertex(v0x, v0y, v0z);
            Vertex b = new Vertex(v1x, v1y, v1z);
            Vertex c = new Vertex(v2x, v2y, v2z);

            edgeList.add(new Edge(a, b));
            edgeList.add(new Edge(b, c));
            edgeList.add(new Edge(c, a));

            triangleList.add(new Triangle(edgeList, normal));
        }
        poly = new Polyhedron(triangleList);
        return poly;
    }
}
