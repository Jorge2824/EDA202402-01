package trabajoFinal;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class InventarioDigital {
    // Arreglo de Movimientos de Inventario
    private static String[][] movimientos = new String[100][6]; // Matriz bidimensional para guardar los movimientos
    private static int contadorMovimientos = 0;
    // Arreglo de Saldos
    private static String[] codigosProducto = new String[100];
    private static String[] nombresProducto = new String[100];
    private static int[] cantidadesProducto = new int[100];
    private static double[] preciosUnitario = new double[100];
    private static int contadorProductos = 0;

    private static Scanner scanner = new Scanner(System.in);
    private static SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");

    // Método para mostrar el menú principal
    private static void mostrarMenu() {
        System.out.println("\n--- Digitalizador de Inventario ---");
        System.out.println("1. Agregar producto (Ingreso)");
        System.out.println("2. Mostrar inventario");
        System.out.println("3. Registrar salida de producto");
        System.out.println("4. Mostrar movimientos por rango de fechas");
        System.out.println("5. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private static double redondearDecimales(double numeroDecimal) {
        return Math.round(numeroDecimal * 100) / 100.0;
    }

    // Método para redimensionar el arreglo bidimensional
    private static void redimensionarMovimientos() {
        int nuevoTamano = movimientos.length * 2;
        String[][] nuevosMovimientos = new String[nuevoTamano][6];

        for (int i = 0; i < contadorMovimientos; i++) {
            for (int j = 0; j < movimientos[i].length; j++) {
                nuevosMovimientos[i][j] = movimientos[i][j];
            }
        }

        movimientos = nuevosMovimientos;
    }

    // Método para redimensionar el arreglo de productos
    private static void redimensionarProductos() {
        int nuevoTamano = codigosProducto.length * 2;
        String[] nuevoCodigosProducto = new String[nuevoTamano];
        String[] nuevoNombresProducto = new String[nuevoTamano];
        int[] nuevoCantidadesProducto = new int[nuevoTamano];
        double[] nuevoPreciosUnitario = new double[nuevoTamano];

        for (int i = 0; i < contadorProductos; i++) {
            nuevoCodigosProducto[i] = codigosProducto[i];
            nuevoNombresProducto[i] = nombresProducto[i];
            nuevoCantidadesProducto[i] = cantidadesProducto[i];
            nuevoPreciosUnitario[i] = preciosUnitario[i];
        }

        codigosProducto = nuevoCodigosProducto;
        nombresProducto = nuevoNombresProducto;
        cantidadesProducto = nuevoCantidadesProducto;
        preciosUnitario = nuevoPreciosUnitario;
    }

    // Método para encontrar el producto existente
    private static int encuentraIndice(String codigoProducto) {
        int indice = -1;
        for (int i = 0; i < contadorProductos; i++) {
            if (codigosProducto[i].equals(codigoProducto)) {
                indice = i;
                break;
            }
        }
        return indice;
    }

    // Método para agregar un producto (Ingreso)
    private static void agregarProducto() {
        System.out.print("Ingrese la fecha de ingreso (dd/MM/yyyy): ");
        String fechaIngreso = scanner.nextLine();

        System.out.print("Ingrese el código del producto: ");
        String codigoProducto = scanner.nextLine();

        System.out.print("Ingrese el nombre del producto: ");
        String nombreProducto = scanner.nextLine();

        System.out.print("Ingrese la cantidad: ");
        int cantidadProducto = scanner.nextInt();

        System.out.print("Ingrese el precio unitario: ");
        double precioProducto = scanner.nextDouble();

        if (contadorMovimientos == movimientos.length) {
            redimensionarMovimientos();
        }
        movimientos[contadorMovimientos][0] = codigoProducto;
        movimientos[contadorMovimientos][1] = nombreProducto;
        movimientos[contadorMovimientos][2] = String.valueOf(cantidadProducto);
        movimientos[contadorMovimientos][3] = String.valueOf(precioProducto);
        movimientos[contadorMovimientos][4] = "Ingreso";
        movimientos[contadorMovimientos][5] = fechaIngreso;
        contadorMovimientos++;
        // Kardex Operativa Ingresos
        int productoEncontrado = encuentraIndice(codigoProducto);
        if (productoEncontrado == -1) {
            if (contadorProductos == codigosProducto.length) {
                redimensionarProductos();
            }
            codigosProducto[contadorProductos] = codigoProducto;
            nombresProducto[contadorProductos] = nombreProducto;
            cantidadesProducto[contadorProductos] = cantidadProducto;
            preciosUnitario[contadorProductos] = precioProducto;
            contadorProductos++;
        } else {
            double montoTotalAnterior = cantidadesProducto[productoEncontrado] * preciosUnitario[productoEncontrado];
            double montoFinal = montoTotalAnterior + (cantidadProducto * precioProducto);
            cantidadesProducto[productoEncontrado] += cantidadProducto;
            preciosUnitario[productoEncontrado] = redondearDecimales(
                    montoFinal / cantidadesProducto[productoEncontrado]);
        }
        System.out.println("Producto ingresado con éxito.");
    }

    // Método para registrar salida de un producto
    private static void registrarSalida() {
        System.out.print("Ingrese el código del producto que desea retirar: ");
        String codigoProducto = scanner.nextLine();

        int indiceEncontrado = encuentraIndice(codigoProducto);
        if (indiceEncontrado == -1) {
            System.out.println("Producto no encontrado en el inventario.");
            return;
        }
        int cantidadDisponible = cantidadesProducto[indiceEncontrado];

        System.out.print("Ingrese la cantidad a retirar: ");
        int cantidadRetirada = scanner.nextInt();
        scanner.nextLine();
        if (cantidadRetirada > cantidadDisponible) {
            System.out.println("Cantidad insuficiente en inventario.");
            return;
        }

        System.out.print("Ingrese la fecha de salida (dd/MM/yyyy): ");
        String fechaSalida = scanner.nextLine();

        if (contadorMovimientos == movimientos.length) {
            redimensionarMovimientos();
        }
        String precioAnterior = String.valueOf(preciosUnitario[indiceEncontrado]);
        movimientos[contadorMovimientos][0] = codigoProducto;
        movimientos[contadorMovimientos][1] = nombresProducto[indiceEncontrado];
        movimientos[contadorMovimientos][2] = String.valueOf(cantidadRetirada);
        movimientos[contadorMovimientos][3] = precioAnterior; // Precio no aplicable en salida
        movimientos[contadorMovimientos][4] = "Salida";
        movimientos[contadorMovimientos][5] = fechaSalida;
        contadorMovimientos++;

        // Kardex Operativa Salidas
        double montoTotalAnterior = cantidadesProducto[indiceEncontrado] * preciosUnitario[indiceEncontrado];
        double montoFinal = montoTotalAnterior - (cantidadRetirada * preciosUnitario[indiceEncontrado]);
        cantidadesProducto[indiceEncontrado] -= cantidadRetirada;
        preciosUnitario[indiceEncontrado] = redondearDecimales(
                montoFinal / cantidadesProducto[indiceEncontrado]);
        System.out.println("Salida registrada con éxito. Sale con el precio S/ " + precioAnterior);
    }

    // Método para mostrar el inventario agrupado por código de producto
    private static void mostrarInventario() {
        if (contadorProductos == 0) {
            System.out.println("El inventario está vacío.");
            return;
        }

        System.out.println("\n--- Inventario Actual Agrupado por Código ---");
        for (int i = 0; i < contadorProductos; i++) {
            String codigo = codigosProducto[i];
            String nombre = nombresProducto[i];
            String cantidadTotal = String.valueOf(cantidadesProducto[i]);
            String precioTotal = String.valueOf(preciosUnitario[i]);
            String montoTotal =  String.valueOf(
                    redondearDecimales(cantidadesProducto[i] * preciosUnitario[i]));
            System.out.println("Código: " + codigo + ", Nombre: " + nombre + ", Cantidad: " +
                    cantidadTotal + ", Precio Unitario: S/" + precioTotal + ", Monto Total: S/" + montoTotal);
        }
    }

    public static void main(String[] args) {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            int opcion = scanner.nextInt();
            scanner.nextLine();
            switch (opcion) {
                case 1:
                    agregarProducto();
                    break;
                case 2:
                    mostrarInventario();
                    break;
                case 3:
                    registrarSalida();
                    break;
                case 4:
                    break;
                case 5:
                    continuar = false;
                    System.out.println("Gracias por usar el Digitalizador de Inventario.");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }
}
