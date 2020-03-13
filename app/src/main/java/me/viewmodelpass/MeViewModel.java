package me.viewmodelpass;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MeViewModel extends ViewModel {

    public MutableLiveData<String> content = new MutableLiveData<>();

}
