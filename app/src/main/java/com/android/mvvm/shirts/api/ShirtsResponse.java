

package com.android.mvvm.shirts.api;


import com.android.mvvm.shirts.vo.Shirt;
import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ShirtsResponse {
    @SerializedName("total_count")
    private int total;
    @SerializedName("items")
    private List<Shirt> items;
    private Integer nextPage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Shirt> getItems() {
        return items;
    }

    public void setItems(List<Shirt> items) {
        this.items = items;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    @NonNull
    public List<Integer> getRepoIds() {
        List<Integer> repoIds = new ArrayList<>();
        for (Shirt shirts : items) {
            repoIds.add(shirts.getId());
        }
        return repoIds;
    }
}
