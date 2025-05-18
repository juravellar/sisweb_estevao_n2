package api.util;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

public final class CarrinhoSessionUtil {

    private static final String ATTR = "carrinhoTemp";

    @SuppressWarnings("unchecked")
    public static Map<Integer,Integer> getCarrinho(HttpSession s) {
        Map<Integer,Integer> carrinho =
                (Map<Integer,Integer>) s.getAttribute(ATTR);

        if (carrinho == null) {
            carrinho = new HashMap<>();
            s.setAttribute(ATTR, carrinho);
        }
        return carrinho;
    }

    public static void add(HttpSession s, int idProd) {
        Map<Integer,Integer> cart = getCarrinho(s);
        cart.merge(idProd, 1, Integer::sum);
    }

    public static void remove(HttpSession s, int idProd) {
        Map<Integer,Integer> cart = getCarrinho(s);
        cart.computeIfPresent(idProd, (k,q) -> q > 1 ? q-1 : null);
    }

    public static void clear(HttpSession s) {
        s.removeAttribute(ATTR);
    }
}
