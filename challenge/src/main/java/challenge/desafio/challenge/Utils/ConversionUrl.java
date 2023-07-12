package challenge.desafio.challenge.Utils;

public class ConversionUrl {

    private static final String allowedAlphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private char[] allowedCharacters = allowedAlphabet.toCharArray();
    private int base = allowedCharacters.length;

    public String encodeUrl(Long input){
        var encodedString = new StringBuilder();

        if(input == 0) {
            return String.valueOf(allowedCharacters[0]);
        }

        while (input > 0) {
            encodedString.append(allowedCharacters[(int) (input % base)]);
            input = input / base;
        }

        return encodedString.reverse().toString();
    }

    public long decodeUrl(String input) {
        var characters = input.toCharArray();
        var length = characters.length;

        var decoded = 0;


        var counter = 1;
        for (int i = 0; i < length; i++) {
            decoded += allowedAlphabet.indexOf(characters[i]) * Math.pow(base, length - counter);
            counter++;
        }
        return decoded;
    }
}
