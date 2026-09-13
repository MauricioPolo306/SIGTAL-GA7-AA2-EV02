package com.mycompany.sigtal.dao;

import com.mycompany.sigtal.modelo.Vehiculo;
import com.mycompany.sigtal.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    /*
     * ==========================================================
     * REGISTRAR VEHÍCULO
     * ==========================================================
     */
    public String registrar(Vehiculo vehiculo) {

        String placa = normalizarTexto(vehiculo.getPlaca());

        // Verificar si la placa ya existe
        if (existePlaca(placa, 0)) {
            return "PLACA_DUPLICADA";
        }

        String sql = """
                INSERT INTO vehiculos
                (placa, marca, modelo, anio, color, tipo_vehiculo, id_cliente)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(
                    1,
                    placa
            );

            sentencia.setString(
                    2,
                    normalizarTexto(vehiculo.getMarca())
            );

            sentencia.setString(
                    3,
                    normalizarTexto(vehiculo.getModelo())
            );

            /*
             * Año
             */
            if (vehiculo.getAnio() != null) {

                sentencia.setInt(
                        4,
                        vehiculo.getAnio()
                );

            } else {

                sentencia.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            sentencia.setString(
                    5,
                    normalizarTexto(vehiculo.getColor())
            );

            sentencia.setString(
                    6,
                    normalizarTexto(vehiculo.getTipoVehiculo())
            );

            sentencia.setInt(
                    7,
                    vehiculo.getIdCliente()
            );

            return sentencia.executeUpdate() > 0
                    ? "OK"
                    : "ERROR";

        } catch (SQLException e) {

            System.err.println(
                    "Error al registrar vehículo: "
                    + e.getMessage()
            );

            return "ERROR";
        }
    }

    /*
     * ==========================================================
     * VERIFICAR PLACA DUPLICADA
     * ==========================================================
     */
    private boolean existePlaca(
            String placa,
            int idVehiculo) {

        if (placa == null || placa.isBlank()) {
            return false;
        }

        String sql = """
                SELECT COUNT(*)
                FROM vehiculos
                WHERE UPPER(TRIM(placa)) = UPPER(TRIM(?))
                AND id_vehiculo <> ?
                """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(
                    1,
                    placa
            );

            sentencia.setInt(
                    2,
                    idVehiculo
            );

            try (ResultSet resultado =
                         sentencia.executeQuery()) {

                if (resultado.next()) {

                    return resultado.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al verificar placa: "
                    + e.getMessage()
            );
        }

        return false;
    }

    /*
     * ==========================================================
     * LISTAR TODOS LOS VEHÍCULOS
     * ==========================================================
     */
    public List<Vehiculo> listar() {

        List<Vehiculo> vehiculos =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_vehiculo,
                    placa,
                    marca,
                    modelo,
                    anio,
                    color,
                    tipo_vehiculo,
                    id_cliente
                FROM vehiculos
                ORDER BY id_vehiculo DESC
                """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     sentencia.executeQuery()) {

            while (resultado.next()) {

                vehiculos.add(
                        mapearVehiculo(resultado)
                );
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al listar vehículos: "
                    + e.getMessage()
            );
        }

        return vehiculos;
    }

    /*
     * ==========================================================
     * BUSCAR VEHÍCULO POR ID
     * ==========================================================
     */
    public Vehiculo buscarPorId(int idVehiculo) {

        String sql = """
                SELECT
                    id_vehiculo,
                    placa,
                    marca,
                    modelo,
                    anio,
                    color,
                    tipo_vehiculo,
                    id_cliente
                FROM vehiculos
                WHERE id_vehiculo = ?
                """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(
                    1,
                    idVehiculo
            );

            try (ResultSet resultado =
                         sentencia.executeQuery()) {

                if (resultado.next()) {

                    return mapearVehiculo(
                            resultado
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al buscar vehículo: "
                    + e.getMessage()
            );
        }

        return null;
    }

    /*
     * ==========================================================
     * LISTAR VEHÍCULOS POR CLIENTE
     * ==========================================================
     */
    public List<Vehiculo> listarPorCliente(
            int idCliente) {

        List<Vehiculo> vehiculos =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_vehiculo,
                    placa,
                    marca,
                    modelo,
                    anio,
                    color,
                    tipo_vehiculo,
                    id_cliente
                FROM vehiculos
                WHERE id_cliente = ?
                ORDER BY id_vehiculo DESC
                """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(
                    1,
                    idCliente
            );

            try (ResultSet resultado =
                         sentencia.executeQuery()) {

                while (resultado.next()) {

                    vehiculos.add(
                            mapearVehiculo(resultado)
                    );
                }
            }

        } catch (SQLException e) {

            System.err.println(
                    "Error al consultar vehículos "
                    + "del cliente: "
                    + e.getMessage()
            );
        }

        return vehiculos;
    }

    /*
     * ==========================================================
     * ACTUALIZAR VEHÍCULO
     * ==========================================================
     */
    public String actualizar(Vehiculo vehiculo) {

        String placa =
                normalizarTexto(
                        vehiculo.getPlaca()
                );

        // Verificar si la placa pertenece a otro vehículo
        if (existePlaca(
                placa,
                vehiculo.getIdVehiculo())) {

            return "PLACA_DUPLICADA";
        }

        String sql = """
                UPDATE vehiculos
                SET
                    placa = ?,
                    marca = ?,
                    modelo = ?,
                    anio = ?,
                    color = ?,
                    tipo_vehiculo = ?,
                    id_cliente = ?
                WHERE id_vehiculo = ?
                """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setString(
                    1,
                    placa
            );

            sentencia.setString(
                    2,
                    normalizarTexto(
                            vehiculo.getMarca()
                    )
            );

            sentencia.setString(
                    3,
                    normalizarTexto(
                            vehiculo.getModelo()
                    )
            );

            /*
             * Año
             */
            if (vehiculo.getAnio() != null) {

                sentencia.setInt(
                        4,
                        vehiculo.getAnio()
                );

            } else {

                sentencia.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            sentencia.setString(
                    5,
                    normalizarTexto(
                            vehiculo.getColor()
                    )
            );

            sentencia.setString(
                    6,
                    normalizarTexto(
                            vehiculo.getTipoVehiculo()
                    )
            );

            sentencia.setInt(
                    7,
                    vehiculo.getIdCliente()
            );

            sentencia.setInt(
                    8,
                    vehiculo.getIdVehiculo()
            );

            return sentencia.executeUpdate() > 0
                    ? "OK"
                    : "ERROR";

        } catch (SQLException e) {

            System.err.println(
                    "Error al actualizar vehículo: "
                    + e.getMessage()
            );

            return "ERROR";
        }
    }

    /*
     * ==========================================================
     * ELIMINAR VEHÍCULO
     * ==========================================================
     */
    public boolean eliminar(int idVehiculo) {

        String sql = """
                DELETE FROM vehiculos
                WHERE id_vehiculo = ?
                """;

        try (Connection conexion =
                     ConexionBD.obtenerConexion();
             PreparedStatement sentencia =
                     conexion.prepareStatement(sql)) {

            sentencia.setInt(
                    1,
                    idVehiculo
            );

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {

            System.err.println(
                    "Error al eliminar vehículo: "
                    + e.getMessage()
            );

            return false;
        }
    }

    /*
     * ==========================================================
     * MAPEAR RESULTADO → OBJETO VEHÍCULO
     * ==========================================================
     */
    private Vehiculo mapearVehiculo(
            ResultSet resultado)
            throws SQLException {

        Vehiculo vehiculo =
                new Vehiculo();

        vehiculo.setIdVehiculo(
                resultado.getInt(
                        "id_vehiculo"
                )
        );

        vehiculo.setPlaca(
                resultado.getString(
                        "placa"
                )
        );

        vehiculo.setMarca(
                resultado.getString(
                        "marca"
                )
        );

        vehiculo.setModelo(
                resultado.getString(
                        "modelo"
                )
        );

        int anio =
                resultado.getInt("anio");

        if (resultado.wasNull()) {

            vehiculo.setAnio(null);

        } else {

            vehiculo.setAnio(anio);
        }

        vehiculo.setColor(
                resultado.getString(
                        "color"
                )
        );

        vehiculo.setTipoVehiculo(
                resultado.getString(
                        "tipo_vehiculo"
                )
        );

        vehiculo.setIdCliente(
                resultado.getInt(
                        "id_cliente"
                )
        );

        return vehiculo;
    }

    /*
     * ==========================================================
     * NORMALIZAR TEXTO
     * ==========================================================
     */
    private String normalizarTexto(String texto) {

        if (texto == null) {
            return null;
        }

        texto = texto.trim();

        if (texto.isEmpty()) {
            return null;
        }

        return texto;
    }
}