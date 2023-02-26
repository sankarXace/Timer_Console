import java.util.Scanner;

public class TimerApp {
    private static final int PROGRESS_BAR_WIDTH = 50;

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int timer = getValidTimerValue(scanner);

        System.out.print("\nStarting timer... ");
        Thread timerThread = startTimer(timer);
        timerThread.join();

        System.out.println("\nTimer finished!");
        scanner.close();
    }

    private static int getValidTimerValue(Scanner scanner) {
        int timer = 0;
        while (true) {
            System.out.print("Enter timer value in seconds: ");
            try {
                timer = scanner.nextInt();
                if (timer > 0) {
                    break;
                } else {
                    System.out.println("Timer value must be greater than 0.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid timer value.");
                scanner.nextLine();
            }
        }
        return timer;
    }

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
                elapsedTime = System.currentTimeMillis() - startTime;
                float progress = (float) elapsedTime / (timer * 1000);
                String progressBar = getProgressBar(progress);
                String countdownTimer = getCountdownTimer(timer, elapsedTime);
                System.out.print("\r" + progressBar + " " + countdownTimer);
            }
        });
        thread.start();
        return thread;
    }

    private static String getProgressBar(float progress) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int completed = (int) (progress * PROGRESS_BAR_WIDTH);
        for (int i = 0; i < PROGRESS_BAR_WIDTH; i++) {
            if (i < completed) {
                sb.append("\033[32m=\033[0m");
            } else {
                sb.append("\033[31m-\033[0m");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static String getCountdownTimer(int timer, long elapsedTime) {
        long remainingTime = timer * 1000 - elapsedTime;
        remainingTime = remainingTime / 1000 + 1;
        return String.format("%02d:%02d", remainingTime / 60, remainingTime % 60);
    }
}
