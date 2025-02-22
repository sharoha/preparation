import java.util.*;
import java.util.Arrays.*;
import java.util.stream.*;
import java.util.function.*;

enum Suits {
    CLUBS,
    DIAMONDS,
    HEARTS,
    SPADES
}

enum Face {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(11),
    QUEEN(12),
    KING(13);

    private int faceValue;

    Face(int faceValue) {
        this.faceValue = faceValue;
    }

    public int getFaceValue() {
        return faceValue;
    }
}


record Card(Suits suits, Face face) {};

interface PokerHandStrategy {
    boolean isPokerHand(List<Card> cards);
}


class PokerHandUtil {
    public static Set<Suits> getSuits(List<Card> hand) {
        return hand.stream().map(Card::suits).collect(Collectors.toSet());
    }

    public static List<Integer> getFaceValues(List<Card> hand) {
        return hand.stream().map(Card::face).map(Face::getFaceValue).sorted().toList();
    }
}
class RoyalHandStrategy implements PokerHandStrategy {
    private Set<Integer> royalFlush = Set.of(1, 10, 11, 12, 13);

    @Override
    public boolean isPokerHand(List<Card> cards) {
        boolean sameSuits = PokerHandUtil.getSuits(cards).size() == 1;
        if (!sameSuits) {
            return false;
        }
        return PokerHandUtil.getFaceValues(cards)
            .stream()
            .allMatch(royalFlush::contains);
    }
}

class StraightHandStrategy implements PokerHandStrategy {
    @Override
    public boolean isPokerHand(List<Card> cards) {
        // method to be implemetned
        List<Integer> faceValues = PokerHandUtil.getFaceValues(cards);
        for (int i = 1; i < faceValues.size(); i++) {
            if (faceValues.get(i) != faceValues.get(i - 1) + 1)
                return false;
        }
        return true;
    }
}

class FourOfAKind implements PokerHandStrategy {
    @Override
    public boolean isPokerHand(List<Card> cards) {
        // method to be implemetned
        List<Integer> faceValues = PokerHandUtil.getFaceValues(cards);
        int[] freq = new int[14];
        for (int f: faceValues) {
            freq[f]++;
        }
        for (int fr: freq) {
            if (fr >= 4) {
                return true;
            }
        }
        return false;
    }
}

enum PokerHand {
    ROYAL_FLUSH(0, RoyalHandStrategy::new),
    STRAIGHT(1, StraightHandStrategy::new),
    FOUR_OF_A_KIND(2, FourOfAKind::new);
    // add other hands type with their corresponding strategy

    private int order;
    private Supplier<PokerHandStrategy> pokerHandSupplier;

    PokerHand(int order, Supplier<PokerHandStrategy> pokerHandSupplier) {
        this.order = order;
        this.pokerHandSupplier = pokerHandSupplier;
    }

    public PokerHandStrategy getPokerHandStrategy() {
        return pokerHandSupplier.get();
    }

    public static List<PokerHand> getOrderedPokerStrategy() {
        List<PokerHand> values = Arrays.asList(PokerHand.values());
        return values.stream().sorted(Comparator.comparingInt(a -> a.getOrder())).toList();
    }

    public int getOrder() {
        return order;
    }
}

class Deck {
    public List<Card> getDeck() {
        List<Card> deck = new ArrayList<>();
        for (Suits suits: Suits.values()) {
            for (Face face: Face.values()) {
                deck.add(new Card(suits, face)); 
            }
        }
        return deck;
    }
}
public class PokerResult {
    public static void main(String[] args) {
        List<Card> deck = new Deck().getDeck();
        Random random = new Random();
        int testCase = 10000;
        for (int i = 0; i < testCase; i++) {
            List<Card> hand = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                hand.add(deck.get(random.nextInt(0, 52)));
            }
            tests(hand);
        } 
    }

    public static void tests(List<Card> hand) {
        var pokerHand = PokerHand.getOrderedPokerStrategy()
            .stream()
            .filter(e -> e.getPokerHandStrategy().isPokerHand(hand))
            .findFirst()
            .orElse(null);
        if (pokerHand == null) {
        } else if (pokerHand == PokerHand.STRAIGHT) { // test if this ever gets printed
            printHand(hand);
            System.out.println("Found pockerHand: " + pokerHand.name());

        }
    }

    private static void printHand(List<Card> hand) {
        System.out.println(hand);
    }
}


