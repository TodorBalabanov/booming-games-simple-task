import java.util.Random;

public class Main {

	private static final Random PRNG = new Random();

	private static char view[][] = new char[5][5];

	private static int freeGames = 0;

	private static long won = 0L;

	private static long lost = 0L;

	private static char payouts[][] = { { 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0 },
			{ 100, 5, 10, 50, 100, 5 }, { 250, 10, 50, 100, 150, 10 },
			{ 500, 50, 100, 200, 250, 50 }, };

	private static char strip[][] = {
			{ 'B', 'C', 'A', 'C', 'A', 'W', 'A', 'A', 'C', 'A', 'D', 'C', 'B',
					'B', 'S', 'C', 'A', 'C', 'C', 'B', 'A', 'D', 'B', 'C', 'D',
					'B', 'C', 'A', 'B', 'A', 'B', },
			{ 'A', 'W', 'B', 'A', 'B', 'A', 'D', 'C', 'B', 'S', 'C', 'C', 'A',
					'B', 'A', 'A', 'A', 'A', 'D', 'A', 'C', 'B', 'D', 'B', 'B',
					'A', 'C', 'A', 'A', 'A', 'A', },
			{ 'D', 'B', 'A', 'B', 'D', 'B', 'D', 'A', 'B', 'C', 'S', 'A', 'D',
					'B', 'A', 'W', 'B', 'A', 'D', 'A', 'A', 'A', 'D', 'B', 'A',
					'C', 'A', 'C', 'D', 'D', 'D', },
			{ 'D', 'A', 'A', 'B', 'D', 'B', 'C', 'B', 'A', 'D', 'A', 'A', 'D',
					'D', 'W', 'A', 'D', 'B', 'C', 'B', 'C', 'B', 'C', 'B', 'S',
					'C', 'A', 'D', 'A', 'A', 'D', },
			{ 'C', 'A', 'A', 'W', 'A', 'A', 'B', 'A', 'C', 'B', 'S', 'A', 'A',
					'C', 'C', 'B', 'B', 'B', 'D', 'B', 'B', 'A', 'D', 'A', 'C',
					'C', 'D', 'B', 'D', 'A', 'B', }, };

	private static void spin() {
		for (int i = 0; i < view.length && i < strip.length; i++) {
			int r = PRNG.nextInt(strip[i].length);
			for (int j = 0; j < view[i].length; j++) {
				view[i][j] = strip[i][(r + j) % strip[i].length];
			}
		}
	}

	private static void print() {
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < 5; i++) {
				System.out.print(view[i][j]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}

	private static long wildLineWin(int index) {
		int count = 0;
		for (int i = 0; i < view.length; i++) {
			if (view[i][index] == 'W') {
				count++;
			}
		}

		return payouts[count][0];
	}

	private static long lineWin(int index) {
		char symbol = view[0][index];

		long win = 0;
		if (symbol == 'W') {
			win = wildLineWin(index);
			for (int i = 0; i < view.length; i++) {
				if (view[i][index] != 'W') {
					symbol = view[i][index];
					break;
				}
			}
		}

		if (symbol == 'W') {
			return win;
		}

		int count = 0;
		for (int i = 0; i < view.length; i++) {
			if (view[i][index] == symbol) {
				count++;
			} else if (view[i][index] == 'W') {
				count++;
			} else {
				break;
			}
		}

		switch (symbol) {
		case 'W':
			return Math.max(payouts[count][0], win);
		case 'A':
			return Math.max(payouts[count][1], win);
		case 'B':
			return Math.max(payouts[count][2], win);
		case 'C':
			return Math.max(payouts[count][3], win);
		case 'D':
			return Math.max(payouts[count][4], win);
		}

		return 0;
	}

	private static long linesWin() {
		long result = 0;

		for (int j = 0; j < 5; j++) {
			result += lineWin(j);
		}

		return result;
	}

	private static long scatterWin() {
		int count = 0;

		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				if (view[i][j] == 'S') {
					count++;
				}
			}
		}

		return payouts[count][5];
	}

	private static int numberOfFreeGames() {
		int count = 0;

		for (int i = 0; i < view.length; i++) {
			for (int j = 0; j < view[i].length; j++) {
				if (view[i][j] == 'S') {
					count++;
				}
			}
		}

		switch (count) {
		case 3:
			return 5;
		case 4:
			return 10;
		case 5:
			return 20;
		}

		return 0;
	}

	private static boolean hasX() {
		char symbol = view[0][0];

		for (int i = 0; i < view.length && i < view[i].length; i++) {
			if (view[i][i] != symbol) {
				return false;
			}
			if (view[i][view.length - i - 1] != symbol) {
				return false;
			}
		}

		return true;
	}

	private static void repace() {
		char symbol = view[0][0];

		switch (view[0][0]) {
		case 'A':
			symbol = 'B';
			break;
		case 'B':
			symbol = 'C';
			break;
		case 'C':
			symbol = 'D';
			break;
		case 'D':
			symbol = 'W';
			break;
		}

		for (int i = 0; i < view.length && i < view[i].length; i++) {
			view[i][i] = symbol;
			view[i][view.length - i - 1] = symbol;
		}
	}

	public static void main(String[] args) {
		for (long g = 0; g < 10_000_000; g++) {
			lost += 5;
			spin();
			won += linesWin() + 5 * scatterWin();
			if (hasX() == true) {
				/*
				 * Scatter win is counted only once and no extra free spins
				 * added.
				 */
				repace();
				won += linesWin();
			}

			/*
			 * Free games mode.
			 */
			freeGames = numberOfFreeGames();
			while (freeGames > 0) {
				spin();
				won += linesWin() + 5 * scatterWin();
				if (hasX() == true) {
					/*
					 * Scatter win is counted only once and no extra free spins
					 * added.
					 */
					repace();
					won += linesWin();
				}

				freeGames--;
				freeGames += numberOfFreeGames();
			}
		}

		System.out.println(100D * (double) won / (double) lost);
	}
}
