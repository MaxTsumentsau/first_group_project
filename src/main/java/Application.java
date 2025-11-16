import service.StudentSortingApp;

public class Application {
    public static void main(String[] args) {
        StudentSortingApp app = new StudentSortingApp();
        try {
            app.run();
        } finally {
            app.close();
        }
    }
}
