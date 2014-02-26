package compiler.symbols;

import compiler.commons.Id;
import compiler.commons.Token;

import java.util.Hashtable;

/**
 * @author rajith
 * @version ${Revision}
 */
public class Env {
    private Hashtable table;
    protected Env prev;

    public Env(Env p) {
        table = new Hashtable();
        prev = p;
    }

    public void put(Token w, Id i) {
        table.put(w, i);
    }

    public Id get(Token w) {
        for (Env e = this; e != null; e = e.prev) {
            Id found = (Id) (e.table.get(w));
            if (found != null) return found;
        }
        return null;
    }
}