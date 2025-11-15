package util;

public class StudentApp {
    public static void main(String[] args) {
        StudentSortingApp app = new StudentSortingApp();
        try {
            app.run();
        } finally {
            app.close();
        }
    }
}
