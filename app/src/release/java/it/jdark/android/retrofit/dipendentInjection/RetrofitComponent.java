package it.jdark.android.retrofit.dipendentInjection;

import javax.inject.Singleton;

import dagger.Component;
import it.jdark.android.retrofit.MainActivity;

/**
 * Created on 21/04/16.
 *
 * @Autor jDark
 */

@Singleton
@Component(modules = {RetrofitModule.class, AppModule.class})
public interface RetrofitComponent {
    void inject(MainActivity mainActivity);
}
