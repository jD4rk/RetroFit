package it.jdark.android.retrofit.modelView;

import it.jdark.android.retrofit.pojo.Model;

/**
 * Created by jDark on 12/04/16.
 */
public class ModelView {

    private final Model model;

    public ModelView(Model model) {
        this.model = model;
    }

    public String getName() {
        return model.getName();
    }

    public String getStatus() {
        return model.getWeather().get(0).getDescription();
    }

    public String getHumidity() {
        return model.getMain().getHumidity().toString();
    }

    public String getPressure() {
        return model.getMain().getPressure().toString();
    }

}
