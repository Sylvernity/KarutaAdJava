public class Card{
    private String cardID;
    private String cardName;
    private String wishList;
    private String anime;

    public Card(){
        cardID = "";
        cardName = "";
        wishList = "";
        anime = "";
    }

    public Card(String name){
        cardName = name;
        wishList = "";
        anime = "";
    }

    public Card(String ID, String name, String wishCount, String anime){
        cardID = ID;
        cardName = name;
        wishList = wishCount;
        this.anime = anime;
    }

    // *******
    // GETTERS

    public String getCardID(){
        return cardID;
    }

    public String getCardName(){
        return cardName;
    }

    public String getWishList(){
        return wishList;
    }

    public String getAnime(){
        return anime;
    }

    // *******
    // SETTERS

    public void setCardID(String ID){
        cardID = ID;
    }

    public void setCardName(String name){
        cardName = name;
    }

    public void setWishList(String wishCount){
        wishList = wishCount;
    }

    public void setAnime(String anime){
        this.anime = anime;
    }

    // ********
    // ToString

    public String toString(){
        return cardName + " | WL: " + wishList + " | Anime: " + anime + " | " + cardID;
    }
}