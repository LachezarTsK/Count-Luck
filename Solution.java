import java.util.Scanner;

public class Solution {

	private static int numberOfRows;
	private static int numberOfColumns;
	private static int predictedNumberOfWandWaving;
	private static String[][] grid;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int numberOfTestCases = scanner.nextInt();
		int startRow = 0;
		int startColumn = 0;

		for (int i = 0; i < numberOfTestCases; i++) {

			numberOfRows = scanner.nextInt();
			numberOfColumns = scanner.nextInt();
			grid = new String[numberOfRows][numberOfColumns];
			scanner.nextLine();

			for (int row = 0; row < numberOfRows; row++) {
				String[] nextColumns = scanner.nextLine().split("");

				for (int column = 0; column < numberOfColumns; column++) {
					grid[row][column] = nextColumns[column];

					if (grid[row][column].equals("M")) {
						startRow = row;
						startColumn = column;
					}
				}
			}
			predictedNumberOfWandWaving = scanner.nextInt();
			getResults(startRow, startColumn);
		}
		scanner.close();
	}

	/**
	 * The method checks whether a move to the specified cell is possible.
	 * 
	 * @return true, if a move is possible. Otherwise, false.
	 */
	private static boolean cell_isValidMove(int row, int column, int[][] path) {
		boolean row_isInGrid = row >= 0 && row < numberOfRows;
		boolean column_isInGrid = column >= 0 && column < numberOfColumns;

		if ((row_isInGrid && column_isInGrid) == false) {
			return false;
		}

		boolean cell_isNotBlocked = grid[row][column].equals("X") == false;
		boolean cell_isNotVisited = path[row][column] == 0;

		return cell_isNotBlocked && cell_isNotVisited;
	}

	/**
	 * The method checks whether the specified cell is a decision point, i.e. it is
	 * possible to make a move in two or more different directions from this cell.
	 * 
	 * @return true, if it is a decision point. Otherwise, false.
	 */
	private static boolean cell_isDecisionPoint(int row, int column, int[][] path) {
		int possibleMoves = 0;

		/**
		 * Move up.
		 */
		if (cell_isValidMove(row - 1, column, path)) {
			possibleMoves++;
		}

		/**
		 * Move down.
		 */
		if (cell_isValidMove(row + 1, column, path)) {
			possibleMoves++;
		}

		/**
		 * Move left.
		 */
		if (cell_isValidMove(row, column - 1, path)) {
			possibleMoves++;
		}

		/**
		 * Move right.
		 */
		if (cell_isValidMove(row, column + 1, path)) {
			possibleMoves++;
		}
		return possibleMoves >= 2;
	}

	/**
	 * The method applies backtracking to find the path from the specified start to
	 * the point where the port key is situated.
	 * 
	 * Along the search path, each decision point (i.e. it is possible to move in
	 * two or more different directions) is counted.
	 * 
	 * The counting is done by decreasing by one the variable
	 * "predictedNumberOfWandWaving" at each decision point. If it turns out that
	 * the already counted decision point is on a path that does not lead to 
	 * the port key, i.e. there is a backtracking, for each decision point on 
	 * the backtracking path the variable "predictedNumberOfWandWaving" is corrected by
	 * increasing it with one.
	 * 
	 * Ultimately, only the decision points that are on the path to the port key are
	 * counted. If after this counting, the variable "predictedNumberOfWandWaving"
	 * is equal to zero, then the number of actual decision points is equal to 
	 * the number of the prediction.
	 * 
	 * By the same logic, the searched cells in grid "path" are marked with "1" and
	 * if turns out that these cells do not lead to the goal, they are corrected
	 * back to their default value of "0". Ultimately, only the cells from start to
	 * goal will have a value of "1".
	 * 
	 * @return true, if there is a path from start to goal. Otherwise, false.
	 */
	private static boolean searchPath_to_portKey(int row, int column, int[][] path) {

		/**
		 * Goal is reached.
		 */
		if (cell_isValidMove(row, column, path) && grid[row][column].equals("*")) {
			path[row][column] = 1;
			return true;
		}

		if (cell_isValidMove(row, column, path)) {
			/**
			 * Mark the cell with "1", as a potential path to the goal.
			 */
			path[row][column] = 1;

			/**
			 * Count a decision point.
			 */
			if (cell_isDecisionPoint(row, column, path)) {
				predictedNumberOfWandWaving--;
			}

			/**
			 * Move up.
			 */
			if (searchPath_to_portKey(row - 1, column, path)) {
				return true;
			}

			/**
			 * Move down.
			 */
			if (searchPath_to_portKey(row + 1, column, path)) {
				return true;
			}

			/**
			 * Move left.
			 */
			if (searchPath_to_portKey(row, column - 1, path)) {
				return true;
			}

			/**
			 * Move right.
			 */
			if (searchPath_to_portKey(row, column + 1, path)) {
				return true;
			}

			/**
			 * Correct the count of decision points, if the specified point is on a path not
			 * leading to the port key.
			 */
			if (cell_isDecisionPoint(row, column, path)) {
				predictedNumberOfWandWaving++;
			}

			/**
			 * Correct a cell already marked with "1", by replacing this value with "0", if
			 * the cell is on a path that does not lead to the goal.
			 */
			path[row][column] = 0;
			return false;
		}
		return false;
	}

	/**
	 * The method initiates the search and prints the corresponding results, as
	 * specified by the conditions of the challenge.
	 */
	private static void getResults(int startRow, int startColumn) {
		/**
		 * Each cell on the path from start to goal will be marked 
		 * with the value of "1".
		 */
		int[][] path = new int[numberOfRows][numberOfColumns];

		/**
		 * Variable "path_isPresent" is implemented for completeness and readability.
		 * 
		 * The conditions of the challenge guarantee that there is exactly one path 
		 * from start to goal.
		 */
		boolean path_isPresent = searchPath_to_portKey(startRow, startColumn, path);

		if (path_isPresent) {
			if (predictedNumberOfWandWaving == 0) {
				System.out.println("Impressed");
			} else {
				System.out.println("Oops!");
			}
		}
	}
}
