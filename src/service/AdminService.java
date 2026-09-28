package service;

import model.Category;
import model.Offer;
import model.Product;
import model.Supplier;
import ui.util.FileUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AdminService {
    private static final String CATEGORY_FILE = "categories.txt";
    private static final String SUPPLIER_FILE = "suppliers.txt";
    private static final String PRODUCT_FILE = "products.txt";
    private static final String OFFER_FILE = "offers.txt";

    private List<Category> categories;
    private List<Supplier> suppliers;
    private List<Product> products;
    private List<Offer> offers;

    public AdminService() {
        this.categories = loadCategories();
        this.suppliers = loadSuppliers();
        this.products = loadProducts();
        this.offers = loadOffers();
    }

    public boolean addCategory(Category category) {
        if (findCategoryById(category.getId()).isPresent()) {
            return false;
        }
        categories.add(category);
        saveCategories();
        return true;
    }

    public boolean updateCategory(int id, String newName, String newDescription) {
        Optional<Category> optionalCategory = findCategoryById(id);
        if (!optionalCategory.isPresent()) {
            return false;
        }

        Category category = optionalCategory.get();
        category.setName(newName);
        category.setDescription(newDescription);
        saveCategories();
        return true;
    }

    public boolean deleteCategory(int id) {
        for (Product p : products) {
            if (p.getCategoryId() == id) {
                return false;
            }
        }
        boolean removed = categories.removeIf(category -> category.getId() == id);
        if (removed) {
            saveCategories();
        }
        return removed;
    }

    public List<Category> getAllCategories() {
        return new ArrayList<Category>(categories);
    }

    public Optional<Category> findCategoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return Optional.of(category);
            }
        }
        return Optional.empty();
    }

    public boolean addSupplier(Supplier supplier) {
        if (findSupplierById(supplier.getId()).isPresent()) {
            return false;
        }
        suppliers.add(supplier);
        saveSuppliers();
        return true;
    }

    public boolean updateSupplier(int id, String name, String phone, String email, String address) {
        Optional<Supplier> optionalSupplier = findSupplierById(id);
        if (!optionalSupplier.isPresent()) {
            return false;
        }
        Supplier supplier = optionalSupplier.get();
        supplier.setName(name);
        supplier.setPhone(phone);
        supplier.setEmail(email);
        supplier.setAddress(address);
        saveSuppliers();
        return true;
    }

    public boolean deleteSupplier(int id) {
        for (Product p : products) {
            if (p.getSupplierId() == id) {
                return false;
            }
        }
        boolean removed = suppliers.removeIf(supplier -> supplier.getId() == id);
        if (removed) {
            saveSuppliers();
        }
        return removed;
    }

    public List<Supplier> getAllSuppliers() {
        return new ArrayList<Supplier>(suppliers);
    }

    public Optional<Supplier> findSupplierById(int id) {
        for (Supplier supplier : suppliers) {
            if (supplier.getId() == id) {
                return Optional.of(supplier);
            }
        }
        return Optional.empty();
    }

    public boolean addProduct(Product product) {
        if (findProductById(product.getId()).isPresent()) {
            return false;
        }
        if (!findCategoryById(product.getCategoryId()).isPresent() ||
            !findSupplierById(product.getSupplierId()).isPresent()) {
            return false;
        }
        products.add(product);
        saveProducts();
        return true;
    }

    public boolean updateProduct(int id, String name, int categoryId, int supplierId,
                                 double costPrice, double sellingPrice, int quantity,
                                 LocalDate productionDate, LocalDate expirationDate) {
        Optional<Product> optionalProduct = findProductById(id);
        if (!optionalProduct.isPresent()) {
            return false;
        }
        if (!findCategoryById(categoryId).isPresent() || !findSupplierById(supplierId).isPresent()) {
            return false;
        }
        Product product = optionalProduct.get();
        product.setName(name);
        product.setCategoryId(categoryId);
        product.setSupplierId(supplierId);
        product.setCostPrice(costPrice);
        product.setSellingPrice(sellingPrice);
        product.setQuantity(quantity);
        product.setProductionDate(productionDate);
        product.setExpirationDate(expirationDate);
        saveProducts();
        return true;
    }

    public boolean deleteProduct(int id) {
        boolean removed = products.removeIf(product -> product.getId() == id);
        if (removed) {
            saveProducts();
        }
        return removed;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<Product>(products);
    }

    public Optional<Product> findProductById(int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }

    public List<Product> searchProductsByName(String name) {
        List<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> searchProductsByCategoryId(int categoryId) {
        List<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getCategoryId() == categoryId) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> searchProductsByProductionDate(LocalDate date) {
        List<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getProductionDate().isEqual(date)) {
                result.add(product);
            }
        }
        return result;
    }

    public List<Product> searchProductsByExpirationDate(LocalDate date) {
        List<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (product.getExpirationDate().isEqual(date)) {
                result.add(product);
            }
        }
        return result;
    }

    public boolean reduceProductQuantity(int productId, int amount) {
        Optional<Product> optionalProduct = findProductById(productId);
        if (!optionalProduct.isPresent()) {
            return false;
        }
        Product product = optionalProduct.get();
        if (amount <= 0 || product.getQuantity() < amount) {
            return false;
        }
        product.setQuantity(product.getQuantity() - amount);
        saveProducts();
        return true;
    }

    public boolean addOffer(Offer offer) {
        if (findOfferById(offer.getId()).isPresent()) {
            return false;
        }
        if (!findProductById(offer.getProductId()).isPresent()) {
            return false;
        }
        if (offer.getEndDate().isBefore(offer.getStartDate())) {
            return false;
        }
        offers.add(offer);
        saveOffers();
        return true;
    }

    public List<Offer> getAllOffers() {
        return new ArrayList<Offer>(offers);
    }

    public Optional<Offer> findOfferById(int id) {
        for (Offer offer : offers) {
            if (offer.getId() == id) {
                return Optional.of(offer);
            }
        }
        return Optional.empty();
    }

    public String generateProductsReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n===== PRODUCTS REPORT =====\n");
        if (products.isEmpty()) {
            report.append("No products found.\n");
            return report.toString();
        }
        for (Product product : products) {
            String categoryName = findCategoryById(product.getCategoryId()).map(Category::getName).orElse("Unknown Category");
            String supplierName = findSupplierById(product.getSupplierId()).map(Supplier::getName).orElse("Unknown Supplier");
            report.append("ID: ").append(product.getId())
                    .append(" | Name: ").append(product.getName())
                    .append(" | Category: ").append(categoryName)
                    .append(" | Supplier: ").append(supplierName)
                    .append(" | Quantity: ").append(product.getQuantity())
                    .append(" | Selling Price: ").append(product.getSellingPrice())
                    .append(" | Expiry: ").append(product.getExpirationDate())
                    .append("\n");
        }
        return report.toString();
    }

    public String generateCategoryStatistics() {
        StringBuilder report = new StringBuilder();
        report.append("\n===== CATEGORY STATISTICS =====\n");
        if (categories.isEmpty()) {
            report.append("No categories found.\n");
            return report.toString();
        }
        for (Category category : categories) {
            int totalItems = 0;
            int totalQuantity = 0;
            double totalStockValue = 0;
            for (Product product : products) {
                if (product.getCategoryId() == category.getId()) {
                    totalItems++;
                    totalQuantity += product.getQuantity();
                    totalStockValue += product.getSellingPrice() * product.getQuantity();
                }
            }
            report.append("Category: ").append(category.getName())
                    .append(" | Number of Products: ").append(totalItems)
                    .append(" | Total Quantity: ").append(totalQuantity)
                    .append(" | Stock Value: ").append(String.format("%.2f", totalStockValue))
                    .append("\n");
        }
        return report.toString();
    }

    public String generateProfitReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n===== PROFIT REPORT =====\n");
        if (products.isEmpty()) {
            report.append("No products found.\n");
            return report.toString();
        }
        double totalExpectedProfit = 0;
        for (Product product : products) {
            double profitPerUnit = product.getSellingPrice() - product.getCostPrice();
            double expectedProfit = profitPerUnit * product.getQuantity();
            totalExpectedProfit += expectedProfit;
            report.append("Product: ").append(product.getName())
                    .append(" | Cost: ").append(product.getCostPrice())
                    .append(" | Selling: ").append(product.getSellingPrice())
                    .append(" | Quantity: ").append(product.getQuantity())
                    .append(" | Expected Profit: ").append(String.format("%.2f", expectedProfit))
                    .append("\n");
        }
        report.append("----------------------------------\n");
        report.append("Total Expected Profit = ").append(String.format("%.2f", totalExpectedProfit)).append("\n");
        return report.toString();
    }

    public String generateNearExpiryReport(int daysThreshold) {
        StringBuilder report = new StringBuilder();
        report.append("\n===== NEAR EXPIRY PRODUCTS REPORT =====\n");
        LocalDate today = LocalDate.now();
        List<Product> nearExpiryProducts = new ArrayList<Product>();
        for (Product product : products) {
            if (!product.getExpirationDate().isBefore(today)) {
                long remainingDays = ChronoUnit.DAYS.between(today, product.getExpirationDate());
                if (remainingDays <= daysThreshold) {
                    nearExpiryProducts.add(product);
                }
            }
        }
        nearExpiryProducts.sort(new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return p1.getExpirationDate().compareTo(p2.getExpirationDate());
            }
        });
        if (nearExpiryProducts.isEmpty()) {
            report.append("No near expiry products found.\n");
            return report.toString();
        }
        for (Product product : nearExpiryProducts) {
            long remainingDays = ChronoUnit.DAYS.between(today, product.getExpirationDate());
            report.append("Product: ").append(product.getName())
                    .append(" | Expiry Date: ").append(product.getExpirationDate())
                    .append(" | Remaining Days: ").append(remainingDays)
                    .append("\n");
        }
        return report.toString();
    }

    public String generateLowStockReport(int threshold) {
        StringBuilder report = new StringBuilder();
        report.append("\n===== LOW STOCK REPORT =====\n");
        boolean found = false;
        for (Product product : products) {
            if (product.isLowStock(threshold)) {
                found = true;
                report.append("Product: ").append(product.getName())
                        .append(" | Quantity: ").append(product.getQuantity())
                        .append(" | Threshold: ").append(threshold)
                        .append("\n");
            }
        }
        if (!found) {
            report.append("No low stock products found.\n");
        }
        return report.toString();
    }

    private List<Category> loadCategories() {
        List<Category> list = new ArrayList<Category>();
        List<String> lines = FileUtil.readLines(CATEGORY_FILE);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                list.add(Category.fromFileString(line));
            }
        }
        return list;
    }

    private void saveCategories() {
        List<String> lines = new ArrayList<String>();
        for (Category category : categories) {
            lines.add(category.toFileString());
        }
        FileUtil.writeLines(CATEGORY_FILE, lines);
    }

    private List<Supplier> loadSuppliers() {
        List<Supplier> list = new ArrayList<Supplier>();
        List<String> lines = FileUtil.readLines(SUPPLIER_FILE);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                list.add(Supplier.fromFileString(line));
            }
        }
        return list;
    }

    private void saveSuppliers() {
        List<String> lines = new ArrayList<String>();
        for (Supplier supplier : suppliers) {
            lines.add(supplier.toFileString());
        }
        FileUtil.writeLines(SUPPLIER_FILE, lines);
    }

    private List<Product> loadProducts() {
        List<Product> list = new ArrayList<Product>();
        List<String> lines = FileUtil.readLines(PRODUCT_FILE);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                list.add(Product.fromFileString(line));
            }
        }
        return list;
    }

    private void saveProducts() {
        List<String> lines = new ArrayList<String>();
        for (Product product : products) {
            lines.add(product.toFileString());
        }
        FileUtil.writeLines(PRODUCT_FILE, lines);
    }

    private List<Offer> loadOffers() {
        List<Offer> list = new ArrayList<Offer>();
        List<String> lines = FileUtil.readLines(OFFER_FILE);
        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                list.add(Offer.fromFileString(line));
            }
        }
        return list;
    }

    private void saveOffers() {
        List<String> lines = new ArrayList<String>();
        for (Offer offer : offers) {
            lines.add(offer.toFileString());
        }
        FileUtil.writeLines(OFFER_FILE, lines);
    }
}
