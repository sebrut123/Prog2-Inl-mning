public class Nodes {
    interface GraphNode {
    }

    static class RecordNode implements GraphNode {

        private final String title;

        RecordNode(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    static class LocationNode implements GraphNode {
        private final String name;
        private final double x;
        private final double y;

        public LocationNode(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }
        public double getX(){
            return x;
        }
        public double getY(){
            return y;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return String.format("Location: %s (%.1f %.1f)", name, x, y);
        }
    }

    static class PersonNode implements GraphNode {

        private final String name;

        PersonNode(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
