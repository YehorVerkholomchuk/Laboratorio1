package io.github.some_example_name;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

/**
 * Classe ControllerInput
 *
 * @author Yehor Verkholomchuk
 * @version 20.05.2026
 */
public class ControllerInput implements Common {
    /**
     * Ritorna il controller trovato
     * @return il controller trovato
     */
    public static Controller get() {
        if (Controllers.getControllers().size == 0) return null;
        return Controllers.getControllers().first();
    }

    /**
     * Verifica se il controller è connesso
     * @return true se il controller è connesso
     */
    public static boolean isConnected() {
        return Controllers.getControllers().size > 0;
    }

    /**
     * Gestisce l'asse del controller
     * @param axis l'asse del controller
     * @return true se l'asse è valida
     */
    public static float axis(int axis) {
        Controller c = get();
        if (c == null) return 0f;
        float v = c.getAxis(axis);
        return Math.abs(v) < STICK_DEADZONE ? 0f : v;
    }

    /**
     * Verifica se il pulsante del controller è stato premuto
     *
     * @param button pulsante del controller
     * @return true se il pulsante è stato premuto
     */
    public static boolean justPressed(int button) {
        return ButtonState.justPressed(button);
    }

    /**
     * Verifica se il pulsante del controller è tenuto premuto
     *
     * @param button pulsante del controller
     * @return true se il pulsante è tenuto premuto
     */
    public static boolean pressed(int button) {
        Controller c = get();
        return c != null && c.getButton(button);
    }

    /**
     * Classe ButtonState
     *
     * @author Yehor Verkholomchuk
     * @version 20.05.2026
     */
    public static class ButtonState {
        private static final boolean[] prev = new boolean[32];
        private static final boolean[] curr = new boolean[32];

        /**
         * Aggiorna lo stato del pulsante
         */
        public static void update() {
            Controller c = get();
            for (int i = 0; i < curr.length; i++) {
                prev[i] = curr[i];
                curr[i] = c != null && c.getButton(i);
            }
        }

        /**
         * Verifica se il pulsante corrente è stato premuto
         *
         * @param button pulsante corrente
         * @return true se il pulsante è stato premuto
         */
        public static boolean justPressed(int button) {
            return button < curr.length && curr[button] && !prev[button];
        }
    }
}
