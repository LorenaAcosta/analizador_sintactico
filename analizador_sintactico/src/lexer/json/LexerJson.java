/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexer.json;

import com.sun.org.apache.xerces.internal.impl.dv.xs.TypeValidator;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Lore
 */
public class LexerJson {
    
    static TokenValidator tk= new TokenValidator(0, false);

   
    String traduccion ="";
    static String valorId ="";
    static char token;
    static String resultado ="";
   static ArrayList<String> lista = new ArrayList<String>();
    
    static BufferedReader br = null;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      String ubicacionArchivo= "D:\\LCIK-\\6to semestre y 7mo semestre\\Compiladores\\tarea_1\\fuente.txt";
      File archivo = null;
      FileReader fr = null;
    
      try {
         // Apertura del fichero y creacion de BufferedReader para poder
         // hacer una lectura comoda (disponer del metodo readLine()).
         archivo = new File (ubicacionArchivo);
         fr = new FileReader (archivo);
          // Lectura del fichero
         br = new BufferedReader(fr);
         String linea;
        while((linea=br.readLine())!= null){ //lee la linea
            resultado = resultado + linea;
        }
         // System.out.print(resultado +'\n');
        

         getToken();
         json();
         if (lista.size()==0){
             System.out.println("El Json es sintacticamente correcto");
         }else{
         
          Iterator<String> iterador = lista.iterator();
            while(iterador.hasNext()){
                    String elemento = iterador.next();
                    System.out.print(elemento+"\n");
                    
            }
         }
      }
      catch(Exception e){
         e.printStackTrace();
      }finally{
         // En el finally cerramos el fichero, para asegurarnos
         // que se cierra tanto si todo va bien como si salta 
         // una excepcion.
         try{                    
            if( null != fr ){   
               fr.close();     
            }                  
         }catch (Exception e2){ 
            e2.printStackTrace();
         }
      }

    }
    
  
    public static void getToken(){
        tk.pos = tk.pos+1; //1
        if(resultado.length() != tk.pos){
           token = resultado.charAt(tk.pos); //' '
        }
        if (resultado.length()==tk.pos){
            System.out.println("Json unfinished");
        }else{
        //System.out.println(token);
        String val = token +"";

         while (token== ' '|| val.isEmpty()){
             tk.pos = tk.pos+1;
             token = resultado.charAt(tk.pos);
         }
    }
    }
        
     public static void match_token(char token){ //'{'
       String[] lexemas = new String[14];
       String[] compLexico = new String[12];
       lexemas[0]="["; compLexico[0]="L_CORCHETE ";
       lexemas[1]="]"; compLexico[1]="R_CORCHETE ";
       lexemas[2]="{"; compLexico[2]="L_LLAVE ";   
       lexemas[3]="}"; compLexico[3]="R_LLAVE ";   
       lexemas[4]=","; compLexico[4]="COMA ";   
       lexemas[5]="string"; compLexico[5]="LITERAL_CADENA ";   
       lexemas[6]="number"; compLexico[6]="LITERAL_NUMBER ";   
       lexemas[7]="true"; compLexico[7]="PR_TRUE ";   
       lexemas[8]="false"; compLexico[8]="PR_FALSE ";
       lexemas[9]="null"; compLexico[9]="PR_NULL ";   
       lexemas[10]=":"; compLexico[10]="DOS_PUNTOS ";   
       lexemas[11]="TRUE";   
       lexemas[12]="FALSE"; 
       lexemas[13]="NULL"; 
        boolean match= false;
        compLexico[11] ="ERROR_LEXICO ";
        
        for (int i=0; i< lexemas.length ; i++){
              if (lexemas[i].equals(Character.toString(token)) ){
                // System.out.println("Match Token: " + lexemas[i]);
                 match=true;
                 break;
             }
        }
        if (match == true){
            getToken();
        }
     }
   
   
    public static TokenValidator sigTokenString( int inicio, String linea){
        TokenValidator n = new TokenValidator();
        int pos = inicio+1; //entra en uno despues de comilla
        char c = linea.charAt(pos);
        String valXml ="";
        while (c != '"'&& pos<linea.length() ){
            c = linea.charAt(pos);
            valXml+=c;
            pos++;
        }
      //  System.out.println( "<" +valXml+">");   
        n.setPos(pos-1);
        n.setValido(true);
        if(linea.charAt(pos-1) != '"'){
            n.setValido(false);
        }
        
        return n;  
    }
    
    public  static TokenValidator sigTokenNumber( int inicio, String linea){
        TokenValidator tok = new TokenValidator();
        
        char c = linea.charAt(inicio);
        boolean coma = false;
        boolean eChar = false;
        String valNumeric ="";
        while ( (TypeValidator.isDigit(c)||
                "e".equals(linea.charAt(inicio)+"") || 
                "E".equals(linea.charAt(inicio)+"")||
                ".".equals(linea.charAt(inicio)+""))&& inicio < linea.length() && c!=','){
            
              if("e".equals(linea.charAt(inicio)+"") ||"E".equals(linea.charAt(inicio)+"") ){
                  int pos = validar_exponencial(inicio, linea);
                  tok.setPos(pos-1);
                    tok.setValido(false);
                    if(pos!=-1){
                        tok.setValido(true);
                        return tok;
                    }else{
                        return tok;
                       }
              }
              
             if(".".equals(linea.charAt(inicio)+"")){
                int pos =  validar_decimal(inicio+1, linea);
                tok.setPos(pos-1);
                tok.setValido(false);
                if(pos!=-1){
                    tok.setValido(true);
                    return tok;
                }else{
                    return tok;
                   }
              }
             
            
            inicio++;
           
           valNumeric +=c;
            c = linea.charAt(inicio);
            
        }
       // System.out.println(valNumeric +"\n");
         if (linea.charAt(inicio)== ' '|| !TypeValidator.isDigit(c) ){
                tok.setPos(inicio-1);
                tok.setValido(true);
                return tok;
            }
      //  System.out.println(c);
        if (",".equals(c+"") ){
             tok.setPos(inicio);
             tok.setValido(true);
             return tok;
        }
       // tok.setPos(inicio-1);
       // tok.setValido(false);
        if( inicio == linea.length()-1){
            tok.setValido(true);
        }
        return tok;
    }

    private static TokenValidator sigTokenGeneric(int index, String linea, String lexema,String lexemaN) {
        TokenValidator tok= new TokenValidator();
        String cadena = ""; 
        char c = linea.charAt(index);
        while( c!= ' ' && c!='\n' && index<linea.length() && c!=','){
            cadena+=c;
              index++;
            if(index<linea.length())  
            c = linea.charAt(index);
         
        }
        tok.setPos(index-1);
         tok.setValido(lexema.equals(cadena)||lexemaN.equals(cadena));
         return tok;
    }

    private static int validar_decimal(int inicio, String linea) {
        char c = linea.charAt(inicio);
        
        if(!TypeValidator.isDigit(c)){
                return -1;
        }
        while(TypeValidator.isDigit(c)||
                "e".equals(linea.charAt(inicio)+"") || 
                "E".equals(linea.charAt(inicio)+"") && inicio < linea.length()){
            
            if ("e".equals(linea.charAt(inicio)+"") || 
                "E".equals(linea.charAt(inicio)+"")){
               int pos = validar_exponencial( inicio,  linea);
               return pos;
            }
            inicio++;
            if (inicio==linea.length()){
                break;
            }
            c = linea.charAt(inicio);
        }
        return inicio;
    }

    private static int validar_exponencial(int inicio, String linea){
        int val = inicio+1;
        char c = linea.charAt(val);
        if (c=='+' || c=='-'){
            val+=1;
            c = linea.charAt(val);
        }
        if(!TypeValidator.isDigit(c)){
            return -1;
        }
        while(TypeValidator.isDigit(c) && val < linea.length()){
            val= val +1;
            if (val==linea.length()){
                break;
            }
            c = linea.charAt(val);
        }
        return val;
    }

    
    
    
    
    
    
    
    
 //////////////////////////////////////////////////////////////////////////////////
    
    public static void json(){
         switch(token){
             case('{'):
                 element();
                 break;
             case ('['):
                 element();
                 break;
             default:
                handle_error("json");
                break;
         }
    }
   
     
    private static void element() {
         switch(token){
             case('{'):
                 object();
                 break;
             case ('['):
                 array();
                 break;
             default:
                handle_error("element");
                break;
         }
     }
    private static void object() {
        switch(token){
            case ('{'):
                match_token('{');
                o_prima();
                break;
            default:
                handle_error("object");
        }
    }
    private static void array(){
        switch(token){
             case('['):
                 match_token('[');
                 el_prima();
                 break;
             default:
                 handle_error("array");
         } 
    }
    private static void el_prima(){
        switch(token){
             case(']'):
                 match_token(']');
                 break;
             case ('['):
                element_list();
                match_token(']');
                break;
             case ('{'):
                element_list();
                match_token(']');
                break;
             default:
                 handle_error("el_prima");
         }
    }
     private static void o_prima(){
        switch(token){
             case ('"'):
                atribute_list();
                match_token('}');
                break;
             case('}'):
                 match_token('}');
                 break;
             default:
                 handle_error("o_prima");
         }
    }
    private static void element_list(){
        element();
        e_prima();
    }
    private static void atribute_list(){
        atribute();
        al_prima();
    }
    private static void atribute(){
        atribute_name();
        match_token(':');
        atribute_value();
    }
    private static void e_prima(){
        switch(token){
             case(','):
                 match_token(',');
                 element();
                 e_prima();
                 break;    
         }
    }
   
    private static void al_prima(){
        switch(token){
             case(','):
                 match_token(',');
                 atribute();
                 al_prima();
                 break;
         }
    }
    private static void atribute_name(){
        switch(token){
            case('"'):
                 tk= sigTokenString(tk.pos, resultado);
                if (tk.valido){
                  //  System.out.println("Atribute_name");
                    getToken();
                }else{
                    handle_error("atribute_name");
                }
                break;
            default:
                handle_error("atribute_name");
                break;
        }
       
        
    }
    private static void atribute_value(){
        String[] lexemas = new String[14];
       String[] compLexico = new String[12];
       lexemas[0]="["; compLexico[0]="L_CORCHETE ";
       lexemas[1]="]"; compLexico[1]="R_CORCHETE ";
       lexemas[2]="{"; compLexico[2]="L_LLAVE ";   
       lexemas[3]="}"; compLexico[3]="R_LLAVE ";   
       lexemas[4]=","; compLexico[4]="COMA ";   
       lexemas[5]="string"; compLexico[5]="LITERAL_CADENA ";   
       lexemas[6]="number"; compLexico[6]="LITERAL_NUMBER ";   
       lexemas[7]="true"; compLexico[7]="PR_TRUE ";   
       lexemas[8]="false"; compLexico[8]="PR_FALSE ";
       lexemas[9]="null"; compLexico[9]="PR_NULL ";   
       lexemas[10]=":"; compLexico[10]="DOS_PUNTOS ";   
       lexemas[11]="TRUE";   
       lexemas[12]="FALSE"; 
       lexemas[13]="NULL"; 
       boolean match =false;
        TokenValidator val ;
        if(TypeValidator.isDigit(token)){
            val=sigTokenNumber(tk.pos, resultado);
                 if(val.isValido()){
                    // System.out.println("Match Token: num");
                    tk.pos = val.getPos();
                    tk.valido= val.valido;
                    match=true;
                }  
        }else{
        switch(token){
            case ('{'):
                element();
                break;
            case('['):
                element();
                break;
            case('n'):
                val=sigTokenGeneric(tk.pos, resultado, lexemas[9], lexemas[13]);
                    if ( val.isValido() ){
                       //  System.out.println("Match Token: null");
                    tk.pos=val.getPos();
                    tk.valido= val.valido;
                    match=true;
                }  
                 break;
            case('t'):
                val  = sigTokenGeneric (tk.pos, resultado, lexemas[7],lexemas[11]);
                   if (val.isValido()){
                      //  System.out.println("Match Token: True");
                   tk.pos = val.pos;
                   tk.valido= val.valido;
                   match=true;
                   }
                break;
            case('f'):
                 val =sigTokenGeneric(tk.pos, resultado, lexemas[8],lexemas[12]);
                    if(val.isValido()){
                      //   System.out.println("Match Token: false");
                    tk.pos=val.pos;
                    tk.valido= val.valido;
                    match=true;
                    }
                  break;
            case('"'):
                val = sigTokenString(tk.pos ,resultado);
                if(val.isValido()){
                    //datos+=compLexico[5];
                 //   System.out.println("Match Token: string");
                   tk.pos = val.getPos();
                   tk.valido= val.valido;
                   match=true;
                }
                break;
            default:
                handle_error("atribute_value");
        }
        } //findel else
        if (match == true){
            getToken();
            }
        
    }
    private static void handle_error(String value){
        char error = token;
        String sentencia= "Parser_error, found simbol "+error+" at column " + tk.pos ;
        String resto="";
        boolean continuar=false;
        switch(value){
        case("json"):
            while(token!='{' || token!= '['){
                 if (token=='{' || token=='['){
                   resto = " expected {, [";
                    continuar =true;
                   break;
                 }
                  getToken();
            }
            if ( continuar){
                 json();
                 break;
            }else{
                 break;
            }
         case("element"):
            while(token!='{' || token!= '[' || token!= ',' || token!=']' || token!='}'){
                 if (token=='{'|| token == '[' || token == ',' || token ==']' || token=='}'){
                   resto = " expected token!";
                   continuar =true;
                   break;
                 }
                  getToken();
            }
            if (continuar){
                 element();
                 break;
            }else{
                 break;
            }
        case ("object"):
          while(token!='{'  || token!= ',' || token!=']' || token!='}'){
                 if (token=='{' || token == ',' || token ==']' || token=='}'){
                   resto = " expected token!";
                   continuar =true;
                   break;
                 }
                  getToken();
            }
            if (continuar){
                 object();
                 break;
            }else{
                 break;
            }
         case ("array"):
         while( token!= '[' || token!= ',' || token!=']' || token!='}'){
                 if ( token == '[' || token == ',' || token ==']' || token=='}'){
                   resto = " expected token!";
                   continuar =true;
                   break;
                 }
                  getToken();
            }
            if (continuar){
                 array();
                 break;
            }else{
                 break;
            }
        case ("el_prima"):
         while(token!='[' || token!= ']' || token!= '{'){
                 if (token=='['  || token!= ']' || token!= '{'){
                     resto = " expected token!";
                   break;
                 }
                  getToken();
            }
            if (token=='['  || token== ']' || token== '{'){
                 el_prima();
                 break;
            }else{
                 break;
            }
         case ("o_prima"):
         while(token!='"' ||token!= '}' || token!= ',' || token!=']' || token!='}' ){
                 if (token=='"' || token=='}' || token== ',' || token==']' || token=='}'){
                     resto = " expected token!";
                   break;
                 }
                  getToken();
            }
            if ( continuar){
                 o_prima();
                 break;
            }else{
                 break;
            }
         case("atribute_name"):
              while(token!='"' ||token!= ':'  ){
                 if (token=='"' || token==':' ){
                     resto = " expected token ";
                      continuar=true;
                   break;
                 }
                  getToken();
            }
            if (continuar){
                 atribute_name();
                 break;
            }else{
                 break;
            }
            
         case("atribute_value"):
            while(token!= '{'|| token!= '['||token!='"'|| token!='t'|| token!='f'||token!='n'){
                 if (token== '{'|| token== '['||token=='"'|| token=='t'|| token=='f'||token=='n'){
                     resto = " expected atribute value!";
                     continuar=true;
                   break;
                 }
                  getToken();
            }
            if (continuar ){
                 atribute_value();
                 break;
            }else{
                 break;
            }
    }
     continuar=false;
     sentencia= sentencia+resto;
     lista.add(sentencia);
    }
   

   
   
  
}
