package main.java.game.quest;

import main.java.game.inventory.Item;

/**
 * Implémente la quête d'Orphée basée sur le mythe grec
 * Le joueur doit faire des choix qui détermineront s'il parvient à sauver Eurydice des Enfers
 */
public class OrpheusQuest extends Quest {
    public OrpheusQuest() {
        super("orpheus_quest", 
              "La Descente aux Enfers", 
              "Suivez le chemin d'Orphée dans sa quête pour sauver Eurydice");

        // Création des différents états (étapes) de la quête
        QuestState start = new QuestState("start", 
            "Eurydice, votre bien-aimée, vient de mourir d'une morsure de serpent. " +
            "Que souhaitez-vous faire ?");

        QuestState enterUnderworld = new QuestState("enter_underworld",
            "Vous êtes devant l'entrée des Enfers. Hadès vous observe.");

        QuestState meetHades = new QuestState("meet_hades",
            "Hadès vous propose un marché : vous pouvez ramener Eurydice, " +
            "mais vous ne devez pas vous retourner avant d'avoir quitté les Enfers.");

        QuestState walkBack = new QuestState("walk_back",
            "Vous marchez vers la sortie, Eurydice vous suit. " +
            "Vous entendez ses pas, mais êtes-vous sûr qu'elle est là ?");

        QuestState trust = new QuestState("trust",
            "Vous continuez d'avancer, faisant confiance à Hadès et Eurydice.");

        QuestState doubt = new QuestState("doubt",
            "Le doute vous ronge. Que faire ?");

        QuestState success = new QuestState("success",
            "Vous avez réussi ! Eurydice est revenue parmi les vivants.");

        QuestState failure = new QuestState("failure",
            "Vous vous êtes retourné... Eurydice disparaît à jamais dans les Enfers.");

        // Configuration des choix possibles pour chaque état
        // Chaque choix peut mener à un nouvel état et/ou donner une récompense
        start.addChoice(new QuestChoice(
            "Descendre aux Enfers pour la sauver",
            enterUnderworld
        ));
        start.addChoice(new QuestChoice(
            "Accepter son destin",
            null,
            new QuestReward(10, new Item("lyre", "Lyre d'or", null, "Une lyre magique", false, 1), 50)
        ));

        enterUnderworld.addChoice(new QuestChoice(
            "Jouer de la lyre pour émouvoir Hadès",
            meetHades
        ));
        enterUnderworld.addChoice(new QuestChoice(
            "Supplier Hadès",
            meetHades
        ));

        meetHades.addChoice(new QuestChoice(
            "Accepter le marché",
            walkBack
        ));
        meetHades.addChoice(new QuestChoice(
            "Refuser - c'est trop risqué",
            null,
            new QuestReward(5, null, 20)
        ));

        walkBack.addChoice(new QuestChoice(
            "Continuer d'avancer avec confiance",
            trust
        ));
        walkBack.addChoice(new QuestChoice(
            "Le doute s'installe",
            doubt
        ));

        trust.addChoice(new QuestChoice(
            "Continuer jusqu'à la sortie",
            success
        ));

        doubt.addChoice(new QuestChoice(
            "Se retourner pour vérifier",
            failure
        ));
        doubt.addChoice(new QuestChoice(
            "Résister au doute",
            success
        ));

        // Les choix finaux donnent des récompenses différentes selon la réussite ou l'échec
        success.addChoice(new QuestChoice(
            "Célébrer votre réussite",
            null,
            new QuestReward(100, new Item("eurydice_blessing", "Bénédiction d'Eurydice", null, "Une protection divine", false, 1), 200)
        ));

        failure.addChoice(new QuestChoice(
            "Accepter votre échec",
            null,
            new QuestReward(20, new Item("broken_lyre", "Lyre brisée", null, "Une lyre qui ne jouera plus jamais", false, 1), 50)
        ));

        // Initialise la quête avec l'état de départ
        setState(start);
    }
}