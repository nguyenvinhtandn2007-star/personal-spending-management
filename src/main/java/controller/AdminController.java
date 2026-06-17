package controller;

import client.ClientService;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.DanhMuc;
import model.TaiKhoan;

public class AdminController {
    @FXML private TableView<TaiKhoan> tblTaiKhoan;
    @FXML private TableColumn<TaiKhoan, Integer> colUserId;
    @FXML private TableColumn<TaiKhoan, String> colUsername;
    @FXML private TableColumn<TaiKhoan, String> colFullName;
    @FXML private TableColumn<TaiKhoan, String> colRole;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtFullName;
    @FXML private ComboBox<String> cbRole;

    @FXML private TableView<DanhMuc> tblDanhMuc;
    @FXML private TableColumn<DanhMuc, Integer> colCategoryId;
    @FXML private TableColumn<DanhMuc, String> colCategoryName;
    @FXML private TextField txtTenDanhMuc;

    private final ClientService clientService = ClientService.getInstance();

    @FXML
    public void initialize() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("idTaiKhoan"));
        colUsername.setCellValueFactory(new PropertyValueFactory<>("tenDangNhap"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("vaiTro"));
        colCategoryId.setCellValueFactory(new PropertyValueFactory<>("idDanhMuc"));
        colCategoryName.setCellValueFactory(new PropertyValueFactory<>("tenDanhMuc"));

        cbRole.setItems(FXCollections.observableArrayList("ADMIN", "USER"));
        cbRole.setValue("USER");

        tblTaiKhoan.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, tk) -> {
            if (tk != null) {
                txtUsername.setText(tk.getTenDangNhap());
                txtFullName.setText(tk.getHoTen());
                cbRole.setValue(tk.getVaiTro());
                txtPassword.clear();
            }
        });

        tblDanhMuc.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, dm) -> {
            if (dm != null) {
                txtTenDanhMuc.setText(dm.getTenDanhMuc());
            }
        });

        loadTaiKhoan();
        loadDanhMuc();
    }

    @FXML
    public void themTaiKhoan() {
        TaiKhoan tk = formTaiKhoan();
        Task<Void> task = taskRun(() -> clientService.themTaiKhoan(tk));
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Them tai khoan thanh cong.");
            loadTaiKhoan();
        });
        new Thread(task).start();
    }

    @FXML
    public void capNhatTaiKhoan() {
        TaiKhoan selected = tblTaiKhoan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            thongBao(Alert.AlertType.WARNING, "Chon tai khoan can sua.");
            return;
        }
        TaiKhoan tk = formTaiKhoan();
        tk.setIdTaiKhoan(selected.getIdTaiKhoan());
        Task<Void> task = taskRun(() -> clientService.capNhatTaiKhoan(tk));
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Cap nhat tai khoan thanh cong.");
            loadTaiKhoan();
        });
        new Thread(task).start();
    }

    @FXML
    public void xoaTaiKhoan() {
        TaiKhoan selected = tblTaiKhoan.getSelectionModel().getSelectedItem();
        if (selected == null) {
            thongBao(Alert.AlertType.WARNING, "Chon tai khoan can xoa.");
            return;
        }
        Task<Void> task = taskRun(() -> clientService.xoaTaiKhoan(selected.getIdTaiKhoan()));
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Xoa tai khoan thanh cong.");
            loadTaiKhoan();
        });
        new Thread(task).start();
    }

    @FXML
    public void themDanhMuc() {
        DanhMuc dm = new DanhMuc(0, txtTenDanhMuc.getText().trim());
        Task<Void> task = taskRun(() -> clientService.themDanhMuc(dm));
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Them danh muc thanh cong.");
            loadDanhMuc();
        });
        new Thread(task).start();
    }

    @FXML
    public void capNhatDanhMuc() {
        DanhMuc selected = tblDanhMuc.getSelectionModel().getSelectedItem();
        if (selected == null) {
            thongBao(Alert.AlertType.WARNING, "Chon danh muc can sua.");
            return;
        }
        DanhMuc dm = new DanhMuc(selected.getIdDanhMuc(), txtTenDanhMuc.getText().trim());
        Task<Void> task = taskRun(() -> clientService.capNhatDanhMuc(dm));
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Cap nhat danh muc thanh cong.");
            loadDanhMuc();
        });
        new Thread(task).start();
    }

    @FXML
    public void xoaDanhMuc() {
        DanhMuc selected = tblDanhMuc.getSelectionModel().getSelectedItem();
        if (selected == null) {
            thongBao(Alert.AlertType.WARNING, "Chon danh muc can xoa.");
            return;
        }
        Task<Void> task = taskRun(() -> clientService.xoaDanhMuc(selected.getIdDanhMuc()));
        task.setOnSucceeded(e -> {
            thongBao(Alert.AlertType.INFORMATION, "Xoa danh muc thanh cong.");
            loadDanhMuc();
        });
        new Thread(task).start();
    }

    @FXML
    public void quayLai() {
        try {
            Stage stage = (Stage) tblTaiKhoan.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/TrangChu.fxml"));
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            thongBao(Alert.AlertType.ERROR, e.getMessage());
        }
    }

    private void loadTaiKhoan() {
        Task<java.util.List<TaiKhoan>> task = new Task<>() {
            @Override
            protected java.util.List<TaiKhoan> call() {
                return clientService.getTaiKhoan();
            }
        };
        task.setOnSucceeded(e -> tblTaiKhoan.getItems().setAll(task.getValue()));
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    private void loadDanhMuc() {
        Task<java.util.List<DanhMuc>> task = new Task<>() {
            @Override
            protected java.util.List<DanhMuc> call() {
                return clientService.getDanhMuc();
            }
        };
        task.setOnSucceeded(e -> tblDanhMuc.getItems().setAll(task.getValue()));
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        new Thread(task).start();
    }

    private TaiKhoan formTaiKhoan() {
        TaiKhoan tk = new TaiKhoan();
        tk.setTenDangNhap(txtUsername.getText().trim());
        tk.setMatKhau(txtPassword.getText());
        tk.setHoTen(txtFullName.getText().trim());
        tk.setVaiTro(cbRole.getValue());
        return tk;
    }

    private Task<Void> taskRun(Runnable runnable) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                runnable.run();
                return null;
            }
        };
        task.setOnFailed(e -> thongBao(Alert.AlertType.ERROR, task.getException().getMessage()));
        return task;
    }

    private void thongBao(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
