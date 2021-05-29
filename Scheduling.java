import java.util.Scanner;
/**
 * This program is designed to calculate OS scheduling using FCFS, RR, and SJF 
 * methods
 * @author Taline Mkrtschjan
 * Due 5 May 2021
 *
 */
public class Scheduling {
	static int numProcess;		//Holds number of processes
	static int[][] process;		//2D array to hold processes and scheduling. Rows
								//hold the processes where columns hold calculations
	
	public static void main(String[] args) {
		
		//To use for user input
		Scanner keyboard = new Scanner(System.in);

		int burst = 0;							//Holds burst time provided by user
		int quantum = 0;						//Holds quantum time provided by user
		int i = 0;								//Holds iteration i
		
		do {
			
			//Interact with user and ask for number of processes
			System.out.print("How many processes do you have? ");
		
			//Save user provided process numbers in numProcess
			numProcess = keyboard.nextInt();
			System.out.println();
			
			//To run if processes are more than one
			if (numProcess > 0 ) {
				
				//Assign the arrays' row sizes based on number of processes 
				process = new int[numProcess][7];
			
				//To ask for number of processes and burst times
				for (i = 0; i < numProcess; i++) {
					System.out.print("What is the burst time of process " + (i+1) 
								   + "? ");
					
					//Save burst of each process based on user input
					burst = keyboard.nextInt();
					
					//Assign burst of each process to its 2D array column[0]
					if (burst > 0) {
						process[i][0] = burst;
					}					
					else {
						//Display message to user for an invalid burst input
						System.out.println("That is not a valid burst number. "
										 + "Please enter a positive integer "
										 + "greater than 0.");
					}
					
				}//for loop for processes
				
				do {
					//To ask for quantum time input and assign to quantum variable
					System.out.print("\nWhat is the quantum time? ");
				
					quantum = keyboard.nextInt();
					
					//Display message to user for an invalid quantum input
					if (quantum <= 0) {
						System.out.println("That is not a valid quantum time. "
										 + "Please enter a positive integer "
										 + "greater than 0.");
					}
				//Repeat question for invalid quantum inputs	
				} while (quantum <= 0);
				
			}//if numProcess>0
			
			//Display message to user for an invalid process values
			else {
				System.out.println("That is not a valid process. Please enter a "
								 + "positive integer greater than 0.");
			}
			
		//To repeat question for invalid inputs 
		} while (i < numProcess || numProcess <=0);
		
		
		//Calling all methods 
		FCFS();
		SJF();
		RR(quantum);
		
		//Display results
		displayTable();
		completionTime();
		waitTime();
		
		//To close the keyboard
		keyboard.close();
		
	}

	
	/**
	 *  This method calculates First Come First Serve scheduling values of 
	 *  processes
	 */
	private static void FCFS() {
		
		//Assigning burst values to processes
		for (int j = 0; j < numProcess; j++) {
		
			//To calculate first completion and wait times
			if (j == 0) {
				//Assign completion Time of FCFS to column[1]
				process[j][1] = process[j][0];
				
				//Calculate and assign Average Wait Time of FCFS to column[4]
				process[j][4] = process[j][1] - process[j][0];
				
			}//if 
			
			//To calculate completion and wait time for following processes
			else {
				
				//Assign completion Time of FCFS to column[1] and add
				//previous processes completion times
				process[j][1] = process[j-1][1] + process[j][0];
				
				//Calculate and assign Average Wait Time of FCFS to column[4]
				process[j][4] = process[j][1] - process[j][0];
				
			}//else
		}//for loop
		
	}//FCFS method
	
	
	/**
	 *  This method calculates the scheduling of processes based on the shortest 
	 *  job (burst value) first
	 */
	private static void SJF() {
		
		int sjfTime = 0;						//Holds SJF temp time
		int[] arrayBurst = new int[numProcess];	//To hold the burst times in a 
												//separate array 
		
		//Populate the burst array with burst values to use for SJF
		for (int j = 0; j < numProcess; j++) {
			
			arrayBurst[j] = process[j][0];
		}//for
		
		//To sort burst values for SJF calculation
		sortBurst(arrayBurst);

		//Go through the sorted burst array and compare each value to the original
		//column [0] of the process 2d array. Assign values to SJF completion time
		//and wait time based on ascending order values
		//Goes through the burst array elements
		for (int j = 0; j < arrayBurst.length; j++) {
			
			//Goes through the process 2D array rows and compares column [0] to
			//the elements of burst array
			for (int k = 0; k < numProcess; k++) {
		
				//For first SJF burst calculation assign values to columns 3
				if (j == 0) {
					if (process[k][0] == arrayBurst[j]) {
						process[k][3] = arrayBurst[j];
						
						//Calculate and assign Average Wait Time of SJF to column[6]
						process[k][6] = process[k][3] - process[k][0];
						
						//assign first burst value to sjfTime to use with rest 
						//of processes for completion time
						sjfTime = arrayBurst[j];
						
					}//inner if
				}//if
				
				//For subsequent SJF calculations. Increment sjfTime
				else {
					if (process[k][0] == arrayBurst[j]) {
						process[k][3] = sjfTime + arrayBurst[j];
						
						//Calculate and assign Average Wait Time of SJF to column[6]
						process[k][6] = process[k][3] - process[k][0];
						
						//Increment the completion time of SJF
						sjfTime += arrayBurst[j];
						
					}//if
				}//else
			}//inner for loop with k
			
		}//for loop for SJF with j
	}//SJF method

	
	/**
	 *  This method sorts the burst values in ascending order to be used for the 
	 *  SJF calculation
	 *  @param burstArray - array to be sorted
	 */
	private static void sortBurst(int[] burstArray) {
		
		int temp;							 //Holds temp value to sort and swap
		
		
		//Sort the passed array using Bubble Sort
		for (int i = burstArray.length-1; i >= 0; i--) {
			
			for(int j = 0; j <= i-1; j++) {
				if (burstArray[j] > burstArray[j+1]) {
					temp = burstArray[j];
					burstArray[j] = burstArray[j+1];
					burstArray[j+1] = temp;
				}//if
			}//for
		}//for
	}//sortBurst
	
	
	/**
	 *  This method calculates scheduling of processes based on Round Robin method
	 *  @param quantum - quantum time
	 */
	private static void RR(int quantum) {
		
		int totalBurst = 0;					//Holds total burst time of all
											//processes for RR
		int rrTime = 0;						//Holds accumulation of quantum time
		
		
		//To create another array and populate with burst values to manage 
		//quantum iterations
		int[] rrBurst = new int[numProcess];
	
		//To populate the array with burst values and calculate total burst time
		for (int k = 0; k < numProcess; k++) {
			
			rrBurst[k] = process[k][0];
			totalBurst+= rrBurst[k];
		}
		
		//Round Robin calculations of completion time and wait time. Go through
		//the array of bursts if j is less than total burst values
		for (int j = 0; j <= totalBurst; j++) {

				//To account for bursts larger than quantum. Use mod to loop
				//through the arrays continuously as long as j is <= totalBurst
				if (rrBurst[j%numProcess] - quantum >= 0) {
					
					//Increment rrTime to use for calculation of completion time
					rrTime += quantum;
					
					//Assign rrTime to column [2] of 2D array process
					process[j%numProcess][2] = rrTime;
					
					//Calculate and assign Average Wait Time of RR to column[5]
					process[j%numProcess][5] = process[j%numProcess][2]- 
											   process[j%numProcess][0];
					
					//Decrement remaining burst time from the burst array
					rrBurst[j%numProcess] -= quantum;
				}
				
				//To skip over bursts that are completed
				else if (rrBurst[j%numProcess] == 0) {
					continue;
				}
				
				//To account for burst values that are less than quantum value
				else if (rrBurst[j%numProcess] - quantum < 0) {
					
					//Holds the remaining value of burst time
					int partialQuantum = rrBurst[j%numProcess];
					
					//increment completion rrTime by the partial burst value
					rrTime += partialQuantum;
					
					//Assign rrTime to column [2] of 2D array process
					process[j%numProcess][2] = rrTime;
					
					//Calculate and assign Average Wait Time of RR to column[5]
					process[j%numProcess][5] = process[j%numProcess][2] - 
											   process[j%numProcess][0];
					
					//Decrement remaining burst time from the burst array
					rrBurst[j%numProcess] -= rrBurst[j%numProcess];
				}
		}//For loop for RR
	}//Round Robin method

	
	/**
	 *  This method displays a scheduling table
	 */
	private static void displayTable() {
		
		//To display everything in a table
		System.out.println();
		
		System.out.println("\t\t\t     Completion Time        Average Wait Time");
		System.out.println("\t\t\t     ===============        =================");
		System.out.println("Processes   Burst Time \t    FCFS    RR     SJF     "
						 + " FCFS    RR     SJF");
		System.out.println("=========   ========== \t   ====================    "
						 + "====================");
		
		//Display processes, bursts, completion times, and average times of each
		for (int j = 0; j < process.length; j++) {
			System.out.print("Process " + (j+1)+ "\t");
			
			for (int k = 0; k < process[j].length; k++) {
				System.out.print(process[j][k] + " \t    ");
			}
			
			System.out.println();
		}
		
		System.out.println("=========   ========== \t   ====================    "
						 + "====================");
		
		System.out.print("Total " + numProcess + "      Averages  =  ");
		
	}//displayTable method
	
	
	/**
	 *  This method calculates the completion time of process and their averages
	 */
	private static void completionTime () {
		
		//Holds array of average completion time
		double[] avgCompTime = new double[3];   
		
		//Save values of all completion times of different scheduling methods
		//in an array and calculate average completion time of each method
		//To go through the average completion time array
		for (int j = 0; j < avgCompTime.length; j++) {
			
			//To go through the process 2D array
			for (int k = 0; k < process.length; k++) {
				
				//Increment the average completion times of each method
				avgCompTime[j] += process[k][j+1];
			
			}//for loop: inner
			
			//Divide the average completion time by the number of processes to
			//find the averages of each method and assign them to a subscript
			avgCompTime[j] = avgCompTime[j]/numProcess;
		
		}//for loop: outer
		
		//To display average completion time of all processes
		for (int q = 0; q < avgCompTime.length; q++) {
			if (q < avgCompTime.length-1) {
				System.out.printf("%.2f%3s",avgCompTime[q]," ");
			}
			else {
				System.out.printf("%.2f",avgCompTime[q]);
			}	
		}//for
	}//CompletionTime method
	
	
	/**
	 *  This method calculates the wait time of processes and their averages
	 */
	private static void waitTime() {
		
		//Holds array of average wait time
		double[] avgWaitTime = new double[3];	
		
		//Save values of all wait times of different scheduling methods in 
		//an array and calculate average wait time of each method
		//To go through the average wait time array
		for (int j = 0; j < avgWaitTime.length; j++) {
			
			//To go through the process 2D array
			for (int k = 0; k < process.length; k++) {
				
				//Increment the average wait times of each method
				avgWaitTime[j] += process[k][j+4];
			}
			
			//Divide the average wait times by the number of processes to find
			//the averages of each method and assign the them to a subscript
			avgWaitTime[j] = avgWaitTime[j]/numProcess;
		}
		
		//To display average wait time of all processes
		for (int j = 0; j < avgWaitTime.length; j++) {
			System.out.printf("  %1s%.2f"," ", avgWaitTime[j]);
		}
	}//waitTime method
}//class
