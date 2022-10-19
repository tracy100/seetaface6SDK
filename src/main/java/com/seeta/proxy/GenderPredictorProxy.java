package com.seeta.proxy;

import com.seeta.pool.GenderPredictorPool;
import com.seeta.pool.SeetaConfSetting;
import com.seeta.sdk.GenderPredictor;
import com.seeta.sdk.SeetaImageData;
import com.seeta.sdk.SeetaPointF;

public class GenderPredictorProxy {

    private GenderPredictorPool pool;

    private GenderPredictorProxy() {
    }

    public GenderPredictorProxy(SeetaConfSetting confSetting) {

        pool = new GenderPredictorPool(confSetting);
    }

    public GenderItem predictGenderWithCrop(SeetaImageData image, SeetaPointF[] points) {

        GenderPredictor.GENDER[] gender = new GenderPredictor.GENDER[1];
        GenderPredictor genderPredictor = null;
        boolean result = false;
        try {

            genderPredictor = pool.borrowObject();
            result = genderPredictor.PredictGenderWithCrop(image, points, gender);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (genderPredictor != null) {
                pool.returnObject(genderPredictor);
            }
        }

        return new GenderItem(gender[0], result);
    }

    public class GenderItem {
        private GenderPredictor.GENDER gender;
        private boolean result;

        public GenderItem(GenderPredictor.GENDER gender, boolean result) {
            this.gender = gender;
            this.result = result;
        }

        public GenderPredictor.GENDER getGender() {
            return gender;
        }

        public void setGender(GenderPredictor.GENDER gender) {
            this.gender = gender;
        }

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
    }

}
