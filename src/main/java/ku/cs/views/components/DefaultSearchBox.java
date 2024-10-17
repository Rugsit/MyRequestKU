package ku.cs.views.components;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class DefaultSearchBox<T> extends VBox {
    protected HBox mainHBox;
    private Map<String,StringExtractor<T>> filterList;
    private Map<String, Comparator<T>> comparatorList;
    protected DefaultComboBox<String> filterBox;
    protected DefaultComboBox<String> compareBox;
    protected DefaultTextField searchBox;
    private StringExtractor<T> selectedExtractor;
    private Comparator<T> selectedComparator;
    private String[] compareTypes;
    private String compareType;
    private List<T> searchItems;
    private List<T> queryItems;
    private String query;
    private double width;
    private double height;


    public DefaultSearchBox(List<T> items,Map<String,StringExtractor<T>> filterList,Map<String,Comparator<T>> comparatorList,double w, double h) {
        this.filterList = new LinkedHashMap<>();//index compatible
        this.filterList.put("ค่าเริ่มต้น", null);//fix first index
        this.filterList.putAll(filterList);

        this.comparatorList = comparatorList;
        this.searchItems = items;
        this.queryItems = new ArrayList<>();
        this.selectedExtractor = null;
        this.selectedComparator = null;
        this.query = "";

        this.compareTypes = new String[]{"ปกติ", "น้อยไปมาก", "มากไปน้อย"};
        this.compareType = null;

        this.width = w;
        this.height = h;

        this.setMaxWidth(w);
        this.setMaxHeight(h);
        mainHBox = new HBox();
        mainHBox.setPrefWidth(w);
        mainHBox.setPrefHeight(h);

        initialize();
    }
    protected void initialize(){
        initSearchBox();
        initFilterBox();
        initCompareBox();

        handleSearchBoxChange();
        handleFilterChange();
        handleCompareChange();

        initStyle();

        filterBox.getSelectionModel().selectFirst();
        mainHBox.getChildren().addAll(compareBox,filterBox,searchBox);
        this.getChildren().addAll(mainHBox);
    }
    //INITIALIZE
    private void initSearchBox(){
        searchBox = new DefaultTextField();
        searchBox.setPromptText("search");
    }
    private void initFilterBox(){
        filterBox = new DefaultComboBox() {
            @Override
            protected void setStringExtractor() {
                super.setStringExtractor();
            }
        };
        filterBox.getItems().addAll(filterList.keySet());
    }
    private void initCompareBox(){
        compareBox = new DefaultComboBox() {
            @Override
            protected void setStringExtractor() {
                super.setStringExtractor();
            }
        };
        compareBox.getItems().clear();
    }
    //HANDLE
    private void handleSearchBoxChange(){
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            query = newValue;
            searchItems();
            searchAction();
        });
    }
    private void handleFilterChange(){
        filterBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            selectedExtractor = filterList.get(newValue);//may be null - handled
            selectedComparator = comparatorList.get(newValue);//may be null - handled
            compareBox.getSelectionModel().clearSelection();
            compareBox.getItems().clear();
            compareType = null;
            if(selectedExtractor != null){
                searchBox.setDisable(false);
                compareBox.setVisible(true);
                compareBox.getItems().addAll(compareTypes);
                compareBox.getSelectionModel().selectFirst();
            }else{
                searchBox.clear();
                searchBox.setDisable(true);
                compareBox.setVisible(false);
            }

            searchItems();
            searchAction();
        });
    }
    private void handleCompareChange(){
        compareBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            compareType = newValue;
            searchItems();
            searchAction();
        });
    }
    //SEARCHING
    protected void searchItems() {
        queryItems.clear();
        if (selectedExtractor == null || query.isEmpty()){
            queryItems.addAll(searchItems);
            return;
        }
        for(T item : searchItems){
            if(selectedExtractor.extract(item).contains(query)){
                queryItems.add(item);
            }
        }

    }
    //FOR OVERRIDING
    protected void searchAction(){
    }
    protected void initStyle(){
        mainHBox.setSpacing(10);
        mainHBox.setAlignment(Pos.CENTER_RIGHT);
        searchBox.setPrefWidth(width * 0.40);
        searchBox.setPrefHeight(height);
        filterBox.setPrefWidth(width * 0.30);
        filterBox.setPrefHeight(height);
        compareBox.setPrefWidth(width * 0.30);
        compareBox.setPrefHeight(height);

        searchBox.setStyle("-fx-background-radius: 10;" +
                "-fx-border-width: 1;" +
                "-fx-border-radius: 10;" +
                "-fx-border-color: black;");
        filterBox.changeBackgroundRadius(10);
        compareBox.changeBackgroundRadius(10);
    }
    public void setSearchItems(List<T> items){
        this.searchItems = items;
    }
    public void forceSearch(){
        searchItems();
        searchAction();
    }
    //GETTER
    public Collection<T> getQueryItems() {
        if(selectedExtractor != null && compareType != null){
            Comparator<T> defaultComparator = new Comparator<>() {//String comparator
                @Override
                public int compare(T o1, T o2) {
                    return selectedExtractor.extract(o1).compareTo(selectedExtractor.extract(o2));
                }
            };
            if (compareType.equalsIgnoreCase(compareTypes[1])){//ascending
                if(selectedComparator != null){
                    Collections.sort(queryItems, selectedComparator);
                }else{
                    Collections.sort(queryItems, defaultComparator);
                }
            }
            if (compareType.equalsIgnoreCase(compareTypes[2])){//descending

                if(selectedComparator != null){
                    Collections.sort(queryItems, selectedComparator.reversed());
                }else{
                    Collections.sort(queryItems, defaultComparator.reversed());
                }
            }
        }


        return queryItems;
    }
}
