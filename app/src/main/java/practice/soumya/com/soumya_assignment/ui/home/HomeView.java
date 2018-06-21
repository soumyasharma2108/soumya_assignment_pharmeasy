package practice.soumya.com.soumya_assignment.ui.home;


import java.util.List;

import practice.soumya.com.soumya_assignment.models.Data;


public interface HomeView {
    void showWait();

    void removeWait();

    void onFailure(String appErrorMessage);

    void showMoreData(List<Data> results);

    void showData(List<Data> results);

    void setTotalPage(Integer totalPages);
}
