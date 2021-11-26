import domain.*;
import services.AddressService;
import org.apache.commons.cli.*;
import services.ChildService;
import services.EducationService;
import services.ParentsService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (new AddressService().getAddress().size() == 0) {
            System.out.println("mocking data...");
            MockData();
        }

        Options options = new Options();
        Option addParents = new Option("p", "addParents", false, "add parents");
        options.addOption(addParents);
        Option momName = new Option("m", "mom", true, "set mom name");
        options.addOption(momName);
        Option dadName = new Option("d", "dad", true, "set dad name");
        options.addOption(dadName);
        Option setAddress = new Option("a", "setAddress", true, "set address");
        setAddress.setArgs(2);
        options.addOption(setAddress);
        Option setEducational = new Option("e", "setEducational", true, "set educational");
        setEducational.setArgs(2);
        options.addOption(setEducational);
        // name, parents id, age
        Option addChild = new Option("c", "addChild", true, "add child");
        addChild.setArgs(3);
        options.addOption(addChild);

        CommandLineParser parser = new org.apache.commons.cli.BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        if (cmd.hasOption('p')) {
            AddParents(cmd);
        }
        if (cmd.hasOption('c')) {
            AddChild(cmd.getOptionValues('c'));
        }
        if (cmd.hasOption('e')) {
            ChangeEducational(cmd.getOptionValues('e'));
        }
        if (cmd.hasOption('a')) {
            ChangeAddress(cmd.getOptionValues('a'));
        }
    }

    private static void ChangeEducational(String[] args) {
        int childId = Integer.parseInt(args[0]);
        int educationalEstablishmentId = Integer.parseInt(args[1]);
        ChildService childService = new ChildService();
        Child child = childService.getById(childId);
        child.education = new EducationService().getById(educationalEstablishmentId);
        child.update();
    }

    private static void ChangeAddress(String[] args) {
        int parentsId = Integer.parseInt(args[0]);
        int addressId = Integer.parseInt(args[1]);
        ParentsService parentsService = new ParentsService();
        Parents parents = parentsService.getById(parentsId);
        parents.address = new AddressService().getById(addressId);
        parents.update();
    }

    private static void AddChild(String[] args) {
        String fullname = args[0];
        int parentsCoupleId = Integer.parseInt(args[1]);
        int age = Integer.parseInt(args[2]);
        Parents parents = new ParentsService().getById(parentsCoupleId);

        if (parents.address != null) {
            List<Education> educational = new EducationService().getEducation();
            List<Education> fineEducational = educational.stream().filter(x -> parents.address.area.addresses.stream().anyMatch(y -> y.id == x.address.id)).collect(Collectors.toList());
            fineEducational.forEach(x -> System.out.println("Подходящее учебное учреждение! Имя " + x.number + ". Id: " + x.id));
        }

        Child child = new Child(fullname, parents, age);
        child.save();
        System.out.println("Ребенок добавлен. Id " + child.id);
    }

    private static void AddParents(CommandLine args) {
        Parents parents = new Parents();
        if (args.hasOption('m')) {
            String momFullname = args.getOptionValue('m');
            parents.FullnameFirst = momFullname;
        }
        if (args.hasOption('d')) {
            String dadFullname = args.getOptionValue('d');
            parents.FullnameSecond = dadFullname;
        }
        parents.save();
        System.out.println("Родитель(и) добавлен(ы). Id " + parents.id);
    }

    // Делаем 3 района, в каждом районе по 2 учебных учреждения.
    public static void MockData() {
        Area[] areas = new Area[]{
                new Area(),
                new Area(),
                new Area(),
        };
        for (Area area : areas) {
            area.Save();
        }
        int i = 0;
        List<Address> addresses = new ArrayList<>(25);
        for (String addressName : new String[]{
                "76209 Blue Bill Park Center",
                "113 Lakewood Road",
                "164 Bunker Hill Hill",
                "36 Rutledge Circle",
                "15895 Summit Center",
                "414 Warrior Park",
                "155 Bartelt Crossing",
                "04 Sheridan Street",
                "43701 2nd Terrace",
                "3999 Fremont Parkway",
                "59571 Fordem Drive",
                "3576 Spohn Road",
                "16 Lotheville Plaza",
                "2642 Delaware Street",
                "28 Sage Plaza",
                "94 Moulton Parkway",
                "38 Johnson Drive",
                "548 Scott Drive",
                "4829 Bay Center",
                "83 Pepper Wood Junction",
                "8 Anthes Crossing",
                "826 Fairfield Park",
                "44 Dottie Terrace",
                "09502 Oakridge Court",
                "55 Armistice Center",
        }) {
            Address address = new Address(addressName);
            addresses.add(address);
            address.area = areas[i % 3];
            address.Save();
            i++;
        }

        for (i = 0; i < 6; i++) {
            Education education = new Education("School #" + i, addresses.get(i));
            education.Save();
        }
    }
}
