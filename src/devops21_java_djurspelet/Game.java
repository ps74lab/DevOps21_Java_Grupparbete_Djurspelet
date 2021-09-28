package devops21_java_djurspelet;

import java.util.*;


public class Game
{
	private static final String mName = "Djurspelet";
	private static final int ATSTART_MIN_PLAYERS = 2;
	private static final int ATSTART_MAX_PLAYERS = 4;
	private static final int ATSTART_MIN_ROUNDS = 5;
	private static final int ATSTART_MAX_ROUNDS = 30;

	private static Store mStore;
	private static ArrayList<Player> mPlayers;
	private static int mNumOfPlayersRequested;
	private static int mNumOfRoundsRequested;
	private static int mRoundsStillToRun;
	private static int mRoundNumber;


	/**
	* Calls method where game is set up by asking players some values
	* Calls method where main loop runs
	*
	* @author P.S.
	*/
	Game()
	{
		System.out.println( getName() );

		mStore = new Store( "Lantdjursbutiken" );
		mPlayers = new ArrayList<>();

		mRoundNumber = 0;

		setupGame();

		runMainGameLoop();
	}


	/**
	* Asks the user for values to initialize the game with.
	* Asks for number of rounds to play.
	* Asks for number of players in this game.
	*
	* @author P.S.
	*/
	private void setupGame()
	{
		//Following commented out lines are important in the game when testing is over
		//mNumOfPlayersRequested = askForValidNumber( "Hur många spelare?", ATSTART_MIN_PLAYERS, ATSTART_MAX_PLAYERS );
		//mNumOfRoundsRequested = askForValidNumber( "Hur många rundor?", ATSTART_MIN_ROUNDS, ATSTART_MAX_ROUNDS );
		//mRoundsStillToRun = mNumOfRoundsRequested;

		// Ask for player names and add players to the game.
		for ( int i = 0; i < mNumOfPlayersRequested; i++ )
		{
			String lReqPlayerName = askForValidName( "Vad heter spelare #" + ( 1 + i ) + "?" );
			mPlayers.add( new Player( lReqPlayerName ) );
		}

		// For testing only, to generate data
		setupTestData();
	}


	/**
	* To setup test data
	*/
	private void setupTestData()
	{
		mNumOfRoundsRequested = 5;
		mRoundsStillToRun = mNumOfRoundsRequested;
		mPlayers.add( new Player( "Åsa" ) );
		mPlayers.get( 0 ).mAnimals.add( new Cat( AnimalGender.MALE ) );
		mPlayers.get( 0 ).mAnimals.add( new Cat( AnimalGender.FEMALE ) );
		mPlayers.get( 0 ).mAnimals.add( new Horse() );
		mPlayers.get( 0 ).mFoods.add( new CatFood( 10 ) );
		mPlayers.get( 0 ).mFoods.add( new Forage( 50 ) );
		mPlayers.add( new Player( "Östen" ) );
		mPlayers.get( 1 ).mAnimals.add( new Dog() );
		mPlayers.get( 1 ).mAnimals.add( new Rabbit( AnimalGender.MALE ) );
		mPlayers.get( 1 ).mAnimals.add( new Rabbit( AnimalGender.FEMALE ) );
		mPlayers.get( 1 ).mFoods.add( new DogFood( 10 ) );
		mPlayers.get( 1 ).mFoods.add( new Carrots( 10 ) );
//		mPlayers.add( new Player( "Håkan" ) );
//		mPlayers.get( 2 ).mAnimals.add( new Rabbit() );
//		mPlayers.get( 2 ).mAnimals.add( new Cattle( AnimalGender.MALE ) );
//		mPlayers.get( 2 ).mAnimals.add( new Cattle( AnimalGender.FEMALE ) );
//		mPlayers.get( 2 ).mFoods.add( new Carrots( 10 ) );
//		mPlayers.get( 2 ).mFoods.add( new Forage( 50 ) );
		mNumOfPlayersRequested = mPlayers.size();
	}


	/**
	* Asks for user input. Loops until a valid character has been entered
	* Shows a notice if value is not valid.
	*
	* @param pMsg         Message shown on the screen
	* @param pValidChars  List of valid chars as a plain string
	* @return             The character entered
	*
	* @author P.S.
	*/
	protected static String askForValidChar( String pMsg, String pValidChars )
	{
		boolean lIsValid = false; // Not yet!
		String lReturnChar = "";
		pValidChars = pValidChars.toUpperCase();
		String lRegExStr = "[" + pValidChars + "]";  // Square brackets are for matching one of possible chars
		Scanner lScanner = new Scanner( System.in );

		while ( !lIsValid ) // Keep asking for valid choice
		{
			// Show the message on screen
			System.out.print( "\n" + pMsg + " Ange (" + pValidChars + "): " );

			// Get input from user
			String lInputStr = lScanner.nextLine();
			lInputStr = lInputStr.toUpperCase();

			// Validate input with regular expression
			lIsValid = lInputStr.matches( lRegExStr );

			// Check if input is valid
			if ( lIsValid )
			{
					lReturnChar = String.valueOf( lInputStr.charAt( 0 ) );
			}
			else
			{
					System.out.println( "Alternativet finns inte." );
			}
		}

		return lReturnChar;
	}


	/**
	* Asks for user input. Loops until a valid value has been entered
	* Shows a notice if value is not valid.
	*
	* @param pMsg Message shown on th screen
	* @return Validated string
	*
	* @author P.S.
	*/
	protected static String askForValidName( String pMsg )
	{
		boolean lIsValid = false; // Not yet!
		Scanner lScanner = new Scanner( System.in );
		String lInputStr = "";

		String lRegExStr = "[A-ZÅÄÖ][A-ZÅÄÖa-zåäö -]+"; // Swedish alphabet

		while ( !lIsValid ) // Keep asking for valid choice
		{
			// Show the message on screen
			System.out.print( pMsg + " : " );

			// Get input from user
			lInputStr = lScanner.nextLine();

			// Validate input with regular expression
			lIsValid = lInputStr.matches( lRegExStr );

			if ( !lIsValid )
				System.out.println( "Namnet godtas inte.") ;
		}

		return lInputStr;
	}


	/**
	* Prints to screen valid range.
	* Asks for user input. Loops until a valid value within a range has been entered.
	* Shows a notice if value is outside range.
	*
	* @param pMsg       Message shown on th screen
	* @param pValidMin  Lower limit
	* @param pValidMax  Upper limit
	* @return           A value between pValidMin and pValidMax, inclusive
	*
	* @author P.S.
	*/
	public static int askForValidNumber( String pMsg, int pValidMin, int pValidMax )
	{
		boolean lIsValid = false; // Not yet!
		Scanner lScanner = new Scanner( System.in );
		int lParsedInt = 0;

		if ( pValidMax < pValidMin ) pValidMax = pValidMin;

		while ( !lIsValid ) // Keep asking for a valid choice
		{
			// Print to screen the message and a valid intervall
			System.out.print( "\n" + pMsg + " Ange ett tal mellan " + pValidMin + " och " + pValidMax + ": " );

			// Get input from user
			String lInputStr = lScanner.nextLine();
			String lRegExStr = "/[-0-9]+/";

			// Validate input with regular expression
			lIsValid = lInputStr.matches( lRegExStr );

			try
			{
				lParsedInt = Integer.parseInt( lInputStr );
				lIsValid = true;
			}
			catch ( Exception ignored )
			{}

			// Check if input is valid
			if ( lParsedInt < pValidMin || lParsedInt > pValidMax )
			{
				System.out.println( "Värdet du har angett ligger inte inom intervallet." );
				lIsValid = false;
			}
		}

		return lParsedInt;
	}


	/**
	* Shows a list of options
	* Asks for user input. Loops until a valid value has been entered
	*
	* @param pMsg Message shown on th screen
	* @return Validated string
	*
	* @author P.S.
	*/
	protected static int askForValidChoiceWithDesc( String pMsg, String[] pPlayerChoiceDesc )
	{
		System.out.println( "Dessa val finns:" );

		// Loop through pPlayerChoiceDesc array and write to screen every element
		for (int i = 0; i < pPlayerChoiceDesc.length; i++) {
			String s = pPlayerChoiceDesc[ i ];
			System.out.println( (1 + i) + ": " + s );
		}

		// Reuse method
		return askForValidNumber( pMsg, 1, pPlayerChoiceDesc.length );
	}


	/**
	* Runs the main game loop
	* Until number of players still in this game is 1
	* or all of this game rounds has been run
	*
	* @author P.S.
	*/
	private void runMainGameLoop()
	{
		// begin For testing only
		System.out.println( "\nNumOfPlayersRequested: " + mNumOfPlayersRequested );

		System.out.println( "Players:" );
		for ( int i = 0; i < mPlayers.size(); i++ )
		{
			System.out.println( "lPlayerName #" + ( 1 + i ) + ": " + mPlayers.get( i ).getName() );
		}
		// end For testing only

		// Keep looping until all rounds has run or until all but one player is left
		while ( mRoundsStillToRun > 0 && mPlayers.size() > 1 )
		{
			mRoundNumber++;

			runOneRound();

			// Count down
			mRoundsStillToRun--;
		}

		// Game has ended. Sell off all players' animals
		System.out.println( "\nSpelet är slut." );
		for ( Player p : mPlayers )
		{
			p.sellAll();
		}

		//Sort the players
		this.mPlayers.sort( new Comparator<Player>()
		{
			@Override
			public int compare( Player pLPlayer, Player pRPlyyer )
			{
				// -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
				return pLPlayer.getCredits() > pRPlyyer.getCredits() ? -1 : ( pLPlayer.getCredits() < pRPlyyer.getCredits() ) ? 1 : 0;
			}
		} );

		// Show who won the game
		System.out.println( "\n" + this.mPlayers.get( 0 ).getName() + " har vunnit spelet." );

		// Show ranking
		for ( Player p : this.mPlayers )
		{
			System.out.println( "namn: " + p.getName() + "   " + p.getCredits() );
		}

		System.out.println( "");
		System.out.println( "(For testing only) RoundsStillToRun: " + this.mRoundsStillToRun );
		System.out.println( "(For testing only) mPlayers.size(): " + this.mPlayers.size() );
	}


	/**
	* Runs one game round
	*
	* @author P.S.
	*/
	private void runOneRound()
	{
		System.out.println( "\n" + "=".repeat( 80 ) );

		System.out.println( "\nSpelrunda: " + mRoundNumber + " av " + mNumOfRoundsRequested );

		// Go through the list of players still in the game
		// Remove a player from the game if he or she has no animals and no credits
		// Using an iterator to prevent ConcurrentModificationException
		Iterator<Player> lItrP = this.mPlayers.iterator();
		while ( lItrP.hasNext() )
		{
			Player lP = lItrP.next();

			// Remove animals that has died from players' animal list
			// Using an iterator to prevent ConcurrentModificationException
			Iterator<AnimalBase> lItrA = lP.mAnimals.iterator();
			while ( lItrA.hasNext() )
			{
				AnimalBase lA = lItrA.next();

				if ( lA.getHealth() <= 0 )
				{
					System.out.println( lA.getName() + "s " + lA.getKindStr() + "(" + lA.getName() + ") har dött." );
					lItrA.remove();
				}
			}

			if ( lP.getCredits() <= 0 && lP.mAnimals.isEmpty() )
			{
				System.out.println( "Spelet är slut för " + lP.getName() );
				lItrP.remove();
			}
		}

		for ( int i = 0; i < mPlayers.size(); i++ )
		{
			// Write a line to separate player's turn
			System.out.println( "\n" + "-".repeat( 80 ) );

			// For every player
			Player lCurrentPlayer = mPlayers.get( i );

			System.out.println( "\n" + lCurrentPlayer.getName() + "s tur." );

			// Show what animals the player owns
			lCurrentPlayer.printLivestock();
			lCurrentPlayer.printFoodOwned();
			lCurrentPlayer.printCredits();

			System.out.println( "" );

			// Give a player 5 choices
			String lPlayerChoiceDesc[] =
			{
				"Köpa djur",
				"Köpa mat till djuren",
				"Mata djur",
				"Para djur",
				"Sälja djur"
			};
			// and send these choices to askForValidChoiceWithDesc
			switch ( askForValidChoiceWithDesc( lCurrentPlayer.getName() + ", vad vill du göra?", lPlayerChoiceDesc ) )
			{
				case 1:
					mStore.playerEntersAnimalBuyStore( lCurrentPlayer );
					break;
				case 2:
					mStore.playerEntersFoodStore( lCurrentPlayer) ;
					break;
				case 3:
					// Player tries feeding an animal
					lCurrentPlayer.tryAnimalFeeding();
					break;
				case 4:
					// Player tries animal breeding
					lCurrentPlayer.tryAnimalBreeding();
					break;
				case 5:
					// Player selling an animal
					mStore.playerEntersAnimalSellStore( lCurrentPlayer );
					break;
			}

			// Every animal the player owns grows older
			lCurrentPlayer.decayAnimalsOwned();

		} // Player's turn loop end

		System.out.println( "\nFor testing only: Game round step ended." );
	}

	static String getName()
	{
		return mName;
	}
}
