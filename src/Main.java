import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new Window().setVisible(true);
        });
    }
}

class Window extends JFrame {
    private int gridDimension = 20;
    private final Node[][] nodes = new Node[gridDimension][gridDimension];
    private Node startNode = null;
    private Node endNode = null;
    private JPanel panel;

    public Window() {

        this.setLayout(null);
        this.panel = new JPanel();
        this.panel.setLayout(null);
        this.panel.setBounds(0, 0, 5_000, 5_000);

        for (int i = 0; i < gridDimension; i++) {
            for (int j = 0; j < gridDimension; j++) {
                var point = new Point(j * 800 / gridDimension
                                , i * 800 / gridDimension);
                Node node = new Node(point);
                this.nodes[j][i] = node;
                this.panel.add(node);
            }
        }

        this.panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Node clickedNode = findNodeAt(e.getPoint());
                if (clickedNode != null) {
                    if (startNode == null) {
                        startNode = clickedNode;
                    } else if (endNode == null) {
                        endNode = clickedNode;

                        var startEndCoordinates
                                = findFirstAndLastNodeCoordinates(
                                startNode, endNode
                        );

                        List<Point> shortestPath
                                = ShortestPathFinder.findShortestPath(gridDimension
                                , startEndCoordinates[0]
                                , startEndCoordinates[1]);

                        for (int i = 1; i < Objects.requireNonNull(shortestPath).size(); i++) {
                            connectNodes(nodes[shortestPath.get(i - 1).x][shortestPath.get(i - 1).y]
                                    , nodes[shortestPath.get(i).x][shortestPath.get(i).y]);
                        }



                        startNode = null;
                        endNode = null;
                    }
                }
            }
        });


        this.add(this.panel);

        this.setBounds(new Rectangle(400, 10, 800, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private Node findNodeAt(Point point) {
        for (Node[] nodeArr : nodes) {
            for (Node node : nodeArr)
            {
                if (node.getBounds().contains(point)) {
                    return node;
                }
            }
        }

        return null;
    }

    private Point[] findFirstAndLastNodeCoordinates(Node startNode, Node endNode)
    {
        var points = new Point[2];
        for (int i = 0; i < this.gridDimension; i++) {
            for (int j = 0; j < this.gridDimension; j++) {
                if (this.nodes[j][i] == startNode)
                    points[0] = new Point(j, i);
                if (this.nodes[j][i] == endNode)
                    points[1] = new Point(j, i);
            }
        }
        return points;
    }

    private void connectNodes(Node startNode, Node endNode) {


        Graphics g = panel.getGraphics();
        g.setColor(Color.BLACK);
        g.drawLine(
                startNode.getX() + startNode.getWidth() / 2,
                startNode.getY() + startNode.getHeight() / 2,
                endNode.getX() + endNode.getWidth() / 2,
                endNode.getY() + endNode.getHeight() / 2
        );
    }
}

class Node extends JPanel {
    Point coordinate;

    public Node(Point coordinate) {
        this.coordinate = coordinate;
        this.setBounds(
                (int) this.coordinate.getX(),
                (int) this.coordinate.getY(),
                15, 15
        );
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        var g2 = (Graphics2D) g;
        int midX = this.getWidth() / 2;
        int midY = this.getHeight() / 2;
        int circleDiameter = 4;

        int circleX = midX - circleDiameter / 2;
        int circleY = midY - circleDiameter / 2;

        g2.drawOval(circleX, circleY, circleDiameter, circleDiameter);
        g2.fillOval(circleX, circleY, circleDiameter, circleDiameter);
    }
}
