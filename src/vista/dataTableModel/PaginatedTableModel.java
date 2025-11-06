package vista.dataTableModel;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PaginatedTableModel<T> {

    // Lista completa y página actual 
    private final List<T> allData = new ArrayList<>();
    private final List<T> currentPageData = new ArrayList<>();

    // Modelo de tabla al que se le inyectará el "slice" de la página
    private final AbstractTableModel tableModel;

    // Tamaño de página, página actual y total de páginas
    private int pageSize;
    private int currentPage;
    private int totalPages;

    // Contrato para "inyectar" la lista visible al modelo concreto
    @FunctionalInterface
    public interface DataLoader<T> {

        void load(AbstractTableModel model, List<T> data);
    }

    private final DataLoader<T> dataLoader;

    public PaginatedTableModel(AbstractTableModel model, DataLoader<T> loader, int pageSize) {
        this.tableModel = model;
        this.dataLoader = loader;
        this.pageSize = Math.max(1, pageSize);
        this.currentPage = 1;
        this.totalPages = 1;
    }

    // Cargar todos los datos y preparar la primera página
    public void loadAllData(List<T> data) {
        allData.clear();
        if (data != null) {
            allData.addAll(data);
        }
        recomputePages();
        currentPage = 1;
        updateCurrentPage();
    }

    // Recalcular cantidad de páginas
    private void recomputePages() {
        totalPages = (int) Math.ceil((double) allData.size() / pageSize);
        if (totalPages <= 0) {
            totalPages = 1;
        }
        if (currentPage > totalPages) {
            currentPage = totalPages;
        }
    }

    // Actualizar el "slice" visible y notificar al TableModel por medio del DataLoader
    private void updateCurrentPage() { // NUEVO
        currentPageData.clear();
        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, allData.size());
        if (start < allData.size()) {
            currentPageData.addAll(allData.subList(start, end));
        }
        dataLoader.load(tableModel, currentPageData);
    }

    // Navegación
    public boolean firstPage() {
        if (currentPage != 1) {
            currentPage = 1;
            updateCurrentPage();
            return true;
        }
        return false;
    }

    public boolean previousPage() {
        if (currentPage > 1) {
            currentPage--;
            updateCurrentPage();
            return true;
        }
        return false;
    }

    public boolean nextPage() {
        if (currentPage < totalPages) {
            currentPage++;
            updateCurrentPage();
            return true;
        }
        return false;
    }

    public boolean lastPage() {
        if (currentPage != totalPages) {
            currentPage = totalPages;
            updateCurrentPage();
            return true;
        }
        return false;
    }

    public boolean goToPage(int page) {
        if (page >= 1 && page <= totalPages) {
            currentPage = page;
            updateCurrentPage();
            return true;
        }
        return false;
    }

    public void setPageSize(int newSize) {
        if (newSize > 0) {
            pageSize = newSize;
            recomputePages();
            updateCurrentPage();
        }
    }

    // Información de estado
    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalRecords() {
        return allData.size();
    }

    public String getPaginationInfo() {
        int start = allData.isEmpty() ? 0 : (currentPage - 1) * pageSize + 1;
        int end = Math.min(currentPage * pageSize, allData.size());
        return String.format("Mostrando %d-%d de %d | Página %d de %d",
                start, end, allData.size(), currentPage, totalPages);
    }

}
