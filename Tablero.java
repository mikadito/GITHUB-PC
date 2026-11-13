import java.util.Scanner;

public class Tablero {
    // Dimensiones del tablero
    static final int FILAS = 10;
    static final int COLUMNAS = 20;
    
    // Caracteres
    static final char PUNTO = '.';
    static final char JUGADOR = 'P';
    static final char TRAMPA = 'X'; 
    static final char ENEMIGO = 'E';
    static final char POCIÓN = '+';
    
    // Matriz del tablero
    static char [][] tablero = new char[FILAS][COLUMNAS];
    
    // Posicion del jugador
    static int filaJugador = 0;
    static int colJugador = 0;
    
    // Variables del juego (Aumentamos las vidas para poder limpiar el tablero)
    static int vidas = 2; 
    static final int CANTIDAD_TRAMPAS = 15;
    static final int CANTIDAD_ENEMIGOS = 15;
    static final int CANTIDAD_POCIONES = 5;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        inicializarTablero();
        colocarElementos();
        
        boolean salir = false;
        while (!salir && vidas > 0) {
            mostrarTablero();
            
            if (contarTrampasRestantes() == 0 && contarEnemigosRestantes() == 0) {
                System.out.println("¡Felicidades! Has sobrevivido y limpiado todo el tablero.");
                break;
            }
            
            System.out.println("Mover: W S A D | Q (salir)");
            System.out.print("Opcion: ");
            String opcion = sc.nextLine().trim().toLowerCase();
            
            switch (opcion) {
                case "w": mover(-1, 0); break;
                case "s": mover(1, 0); break;
                case "a": mover(0, -1); break;
                case "d": mover(0, 1); break;
                case "q": salir = true; break;
                default:
                    System.out.println("Movimiento no válido");
            }
        }
        
        if (vidas <= 0) {
            System.out.println("\nGAME OVER: Te has quedado sin vidas.");
            mostrarTableroFinal();
        }
        
        System.out.println("Fin del juego.");
        sc.close();
    }

    // Llena el tablero de puntos y coloca al jugador en el centro
    static void inicializarTablero() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                tablero[i][j] = PUNTO;
            }
        }
        filaJugador = FILAS / 2;
        colJugador = COLUMNAS / 2;
        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // Coloca las trampas y los enemigos de forma aleatoria
    static void colocarElementos() {
        int trampasColocadas = 0;
        int enemigosColocados = 0;
        int pocionesColocadas = 0;

        while (trampasColocadas < CANTIDAD_TRAMPAS || enemigosColocados < CANTIDAD_ENEMIGOS || pocionesColocadas < CANTIDAD_POCIONES) {
            int f = (int)(Math.random() * FILAS);
            int c = (int)(Math.random() * COLUMNAS);
            
            // Colocar trampa o enemigo si la celda está vacía y no es la posición inicial del jugador
            if (tablero[f][c] == PUNTO && !(f == FILAS / 2 && c == COLUMNAS / 2)) {
                if (trampasColocadas < CANTIDAD_TRAMPAS) {
                    tablero[f][c] = TRAMPA;
                    trampasColocadas++;
                } else if (enemigosColocados < CANTIDAD_ENEMIGOS) {
                    tablero[f][c] = ENEMIGO;
                    enemigosColocados++;
                } else if (pocionesColocadas < CANTIDAD_POCIONES) {
                    tablero[f][c] = POCIÓN;
                    pocionesColocadas++;
                }
            }
        }
    }

    // Imprime la matriz en consola junto con el contador de vidas 
    static void mostrarTablero() {
        System.out.println("\n--- VIDAS: " + vidas + " | Trampas: " + contarTrampasRestantes() + " | Enemigos: " + contarEnemigosRestantes() + " ---");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == TRAMPA || tablero[i][j] == ENEMIGO || tablero[i][j] == POCIÓN)  {
                    System.out.print(PUNTO + " "); // Se ocultan al jugador
                } else {
                    System.out.print(tablero[i][j] + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    // Muestra el tablero revelando trampas y enemigos (para el GAME OVER)
    static void mostrarTableroFinal() {
        System.out.println("\n--- TABLERO REVELADO ---");
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Mueve al jugador validando límites y comprobando impactos
    static void mover(int deltaFila, int deltaCol) {
        int nuevaFila = filaJugador + deltaFila;
        int nuevaCol = colJugador + deltaCol;
        
        // Validar límites del tablero
        if (nuevaFila < 0 || nuevaFila >= FILAS || nuevaCol < 0 || nuevaCol >= COLUMNAS) {
            System.out.println("❌ No puedes salir del tablero.");
            return;
        }
        
        // Borrar posición anterior y actualizar
        tablero[filaJugador][colJugador] = PUNTO;
        filaJugador = nuevaFila;
        colJugador = nuevaCol;
        
        // Comprobar si cayó en una trampa o enemigo o pocion
        if (tablero[filaJugador][colJugador] == TRAMPA) {
            vidas -= 1;
            System.out.println("⚠️ ¡PISASTE UNA TRAMPA! Has perdido 1 vida.");
        } else if (tablero[filaJugador][colJugador] == ENEMIGO) {
            vidas -= 2;
            System.out.println("⚔️ ¡TE ENFRENTASTE A UN ENEMIGO! Has perdido 2 vidas.");
        } else if (tablero[filaJugador][colJugador] == POCIÓN) {
            vidas += 1;
            System.out.println("💖 ¡HAS ENCONTRADO UNA POCIÓN! Has ganado 1 vida.");
        }
        
        tablero[filaJugador][colJugador] = JUGADOR;
    }

    // Cuenta cuántas trampas quedan
    static int contarTrampasRestantes() {
        int contador = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == TRAMPA) {
                    contador++;
                }
            }
        }
        return contador;
    }

    // Cuenta cuántos enemigos quedan
    static int contarEnemigosRestantes() {
        int contador = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == ENEMIGO) {
                    contador++;
                }
            }
        }
        return contador;
    }

    // Cuenta cuántas pociones quedan
    static int contarPocionesRestantes() {
        int contador = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (tablero[i][j] == POCIÓN) {
                    contador++;
                }
            }
        }
        return contador;
    }
}