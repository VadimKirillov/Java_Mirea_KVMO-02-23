package pr3;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.Observable;
import java.util.Random;

public class Main {

    private static final int TEMPERATURE_NORMAL = 25;
    private static final int CO2_NORMAL = 70;

    public static void main(String[] args) {

        TemperatureSensor temperatureSensor = new TemperatureSensor();
        CO2Sensor co2Sensor = new CO2Sensor();
        AlarmSystem alarmSystem = new AlarmSystem();

        temperatureSensor.getObservable().subscribe(alarmSystem);
        co2Sensor.getObservable().subscribe(alarmSystem);

        // Можно добавить дополнительные наблюдатели
        // temperatureSensor.getObservable().subscribe(otherObserver);
        // co2Sensor.getObservable().subscribe(otherObserver);

        temperatureSensor.start();
        co2Sensor.start();
    }

    static class TemperatureSensor {

        private Observable<Integer> observable;

        public TemperatureSensor() {
            observable = Observable.create(emitter -> {
                while (!emitter.isDisposed()) {
                    int temperature = new Random().nextInt(16) + 15;
                    emitter.onNext(temperature);
                    Thread.sleep(1000);
                }
                emitter.onComplete();
            });
        }

        public Observable<Integer> getObservable() {
            return observable;
        }

        public void start() {
            observable.subscribe();
        }
    }

    static class CO2Sensor {

        private Observable<Integer> observable;

        public CO2Sensor() {
            observable = Observable.create(emitter -> {
                while (!emitter.isDisposed()) {
                    int co2Level = new Random().nextInt(71) + 30;
                    emitter.onNext(co2Level);
                    Thread.sleep(1000);
                }
                emitter.onComplete();
            });
        }

        public Observable<Integer> getObservable() {
            return observable;
        }

        public void start() {
            observable.subscribe();
        }
    }

    static class AlarmSystem implements Observer<Integer> {

        @Override
        public void onSubscribe(Disposable d) {
            // Не используется
        }

        @Override
        public void onNext(Integer value) {
            if (value > TEMPERATURE_NORMAL && value <= CO2_NORMAL) {
                System.out.println("Temperature is above normal!");
            } else if (value > CO2_NORMAL && value <= TEMPERATURE_NORMAL) {
                System.out.println("CO2 level is above normal!");
            } else if (value > TEMPERATURE_NORMAL && value > CO2_NORMAL) {
                System.out.println("ALARM!!!");
            }
        }

        @Override
        public void onError(Throwable e) {
            // Не используется
        }

        @Override
        public void onComplete() {
            // Не используется
        }
    }
}