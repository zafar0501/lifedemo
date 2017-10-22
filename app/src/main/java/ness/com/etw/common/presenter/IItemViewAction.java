package ness.com.etw.common.presenter;

public interface IItemViewAction<T> {

    void onItemRowClick(T itemDoInfo, int position);
}
