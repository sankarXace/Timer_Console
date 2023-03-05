import java.util.Scanner;

public class TimerApp {
    private static final int PROGRESS_BAR_WIDTH = 50;

    // Create Scanner object for user input
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws InterruptedException {
        // Get timer value from user input
        int timer = getValidTimerValue();

        // Start timer
        System.out.print("\nStarting timer... ");
        Thread timerThread = startTimer(timer);
        timerThread.join();

        // Display timer finished message
        System.out.println("\nTimer finished!");
    }

    // Get a valid timer value from user input
    private static int getValidTimerValue() {
        int timer = 0;
        while (true) {
            System.out.print("Enter timer value in seconds: ");
            try {
                timer = sc.nextInt();
                if (timer > 0) {
                    break;
                } else {
                    System.out.println("Timer value must be greater than 0.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid timer value.");
                sc.nextLine();
            }
        }
        sc.close();
        return timer;
    }

    // Start the timer in a new thread
    private static Thread startTimer(int timer) {
        Thread thread = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            long elapsedTime = 0;
            while (elapsedTime < timer * 1000) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }

                // Calculate progress and display progress bar and countdown timer
                elapsedTime = System.currentTimeMillis() - startTime;
                float progress = (float) elapsedTime / (timer * 1000);
                String progressBar = getProgressBar(progress);
                String countdownTimer = getCountdownTimer(timer, elapsedTime);
                System.out.print("\r" + progressBar + " " + countdownTimer);
            }
        });

        // Start the thread and return it
        thread.start();
        return thread;
    }

    // Get progress bar string based on current progress
    private static String getProgressBar(float progress) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int completed = (int) ((1 - progress) * PROGRESS_BAR_WIDTH);
        int remaining = PROGRESS_BAR_WIDTH - completed;
        for (int i = 0; i < completed; i++) {
            sb.append("\033[32m█\033[0m"); // green block
        }
        for (int i = 0; i < remaining; i++) {
            sb.append("\033[31m█\033[0m"); // red block
        }
        sb.append("]");
        return sb.toString();
    }

    // Get countdown timer string based on remaining time
    private static String getCountdownTimer(int timer, long elapsedTime) {
        long remainingTime = timer * 1000 - elapsedTime;
        float remainingPercentage = (float) remainingTime / (timer * 1000) * 100;
        remainingPercentage = Math.round(remainingPercentage * 100) / 100f;
        remainingTime = remainingTime / 1000 + 1;
        return String.format("%02d:%02d (%.1f%%)", remainingTime / 60, remainingTime % 60, remainingPercentage);
    }
}
