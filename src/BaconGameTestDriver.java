public class BaconGameTestDriver {

    private static void runSmallTest() {
        BaconGame bg = new BaconGame(true, "Kevin Bacon");
        bg.printActorsByDegree(0, 10);
        System.out.println();
        bg.changeCenter("Alice");
        System.out.println();
        bg.printPathFromName("Nobody");
        System.out.println();
        bg.printPathFromName("Kevin Bacon");
        System.out.println();
        bg.printTopCenters(3);
    }

    private static void runFullTest() {
        BaconGame bg = new BaconGame(false, "Kevin Bacon");
        bg.printPathFromName("Diane Keaton");
        System.out.println();
        bg.printPathFromName("LeVar Burton");
        System.out.println();
        bg.printPathFromName("Buster Keaton");
        System.out.println();
        bg.changeCenter("John Longden");
        System.out.println();
        bg.printPathFromName("Kevin Bacon");
        System.out.println();
        bg.printPathFromName("Buster Keaton");
        System.out.println();
        bg.printTopCenters(30);
        System.out.println();
        bg.printActorsByDegree(76, 110);
        System.out.println();
        bg.printTopCenters(-10);
        System.out.println();
        bg.printActorsByDegree(0, 1);
    }


    public static void main(String[] args) {
        System.out.println("\nRunning small test\n");
        runSmallTest();
        System.out.println("\nRunning full test\n");
        runFullTest();
    }

}
