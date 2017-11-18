/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer.json;
public class Token {
    String lexema;
    String compoLexico;

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public String getCompoLexico() {
        return compoLexico;
    }

    public void setCompoLexico(String compoLexico) {
        this.compoLexico = compoLexico;
    }

}
