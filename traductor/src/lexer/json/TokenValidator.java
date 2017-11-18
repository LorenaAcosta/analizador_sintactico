/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer.json;

/**
 *
 * @author Lore
 */
public class TokenValidator {
        int pos;
        boolean valido;

    public TokenValidator() {
    }

    public TokenValidator(int pos, boolean valido) {
        this.pos = pos;
        this.valido = valido;
    }
        

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isValido() {
        return valido;
    }

    public void setValido(boolean valido) {
        this.valido = valido;
    }
        
}
