package com.example.sentimentalrecommender.objects;

public class EmotionResponseItem {
    public float Excited;
    public float Angry;
    public float Bored;
    public float Fear;
    public float Sad;
    public float Happy;

    public Emotion getMostValuable() {
        float maxValue = Excited;
        Emotion result = Emotion.EXCITED;

        if (Angry > maxValue) {
            maxValue = Angry;
            result = Emotion.ANGRY;
        }

        if (Bored > maxValue) {
            maxValue = Bored;
            result = Emotion.BORED;
        }

        if (Fear > maxValue) {
            maxValue = Fear;
            result = Emotion.FEAR;
        }

        if (Sad > maxValue) {
            maxValue = Sad;
            result = Emotion.SAD;
        }

        if (Happy > maxValue) {
            maxValue = Happy;
            result = Emotion.HAPPY;
        }

        return result;
    }
}
