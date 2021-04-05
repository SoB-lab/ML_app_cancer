import numpy as np
import os
import tensorflow as tf

from tensorflow import keras
from tensorflow.keras import layers
from tensorflow.keras.models import Sequential, Model
from keras.preprocessing import image
from keras.utils import np_utils
from keras.layers.merge import concatenate
from keras.preprocessing.image import ImageDataGenerator

print(tf.__version__)
print(keras.__version__)

batch_size = 32
img_height = 224
img_width = 224

data_dir = "archive/train"
data_dir_test = "archive/test"

'''train_ds = tf.keras.preprocessing.image_dataset_from_directory(
    data_dir,
    validation_split=0.2,
    subset="training",
    seed=123,
    image_size=(img_height, img_width),
    batch_size=batch_size
)

val_ds = tf.keras.preprocessing.image_dataset_from_directory(
  data_dir,
  validation_split=0.2,
  subset="validation",
  seed=123,
  image_size=(img_height, img_width),
  batch_size=batch_size)

test_ds = tf.keras.preprocessing.image_dataset_from_directory(
  data_dir_test,
  image_size=(img_height, img_width),
  batch_size=batch_size)'''

train_datagen = ImageDataGenerator(
    rescale=1./255,
    shear_range=0.2,
    zoom_range=0.2,
    horizontal_flip=True)

test_datagen = ImageDataGenerator(rescale=1./255)

train_ds = train_datagen.flow_from_directory(
        data_dir,
        target_size=(img_height, img_width),
        batch_size=batch_size,
        class_mode='binary')

test_ds = test_datagen.flow_from_directory(
        data_dir_test,
        target_size=(img_height, img_width),
        batch_size=batch_size,
        class_mode='binary')

print(type(train_ds), type(test_ds))



'''data_augmentation = tf.keras.Sequential([
    layers.experimental.preprocessing.Rescaling(1. / 255, input_shape=(img_height, img_width, 3)),
    layers.experimental.preprocessing.RandomFlip("horizontal_and_vertical"),
    layers.experimental.preprocessing.RandomRotation(0.2),
    layers.experimental.preprocessing.RandomZoom(height_factor=(0.2, 0.3)),
])'''

model = Sequential(
#    data_augmentation
)

model.add(layers.Conv2D(8, 3, padding='same', activation='relu'))
model.add(layers.MaxPooling2D())
model.add(layers.Conv2D(16, 3, padding='same', activation='relu'))
model.add(layers.MaxPooling2D())
model.add(layers.BatchNormalization())
model.add(layers.Conv2D(32, 3, padding='same', activation='relu'))
model.add(layers.MaxPooling2D())
model.add(layers.Conv2D(64, 3, padding='same', activation='relu'))
model.add(layers.MaxPooling2D())
model.add(layers.Flatten())
# layers.Dense(1024, activation='relu'),
model.add(layers.Dense(128, activation='relu'))
model.add(layers.Dense(2))

tf.keras.optimizers.Adam(
    learning_rate=0.001,name='Adam'
)

model.compile(optimizer='adam',
              loss=tf.keras.losses.SparseCategoricalCrossentropy(from_logits=True),
              metrics=['accuracy'])

epochs = 1
history = model.fit(
  train_ds,
    epochs=epochs)

model.summary()

print('Saving the model')
model.save('my_model.h5')
print('model saved')

'''predictions = np.array([])
Y_pred = np.array([])
labels = np.array([])
Y_true = np.array([])
for x, y in test_ds:
    predictions = model.predict(x,batch_size=32)
    y_pred = np.argmax(predictions, axis=1)
    Y_pred = np.append(y_pred, Y_pred)
    Y_true = np.append(y, Y_true)

print(tf.math.confusion_matrix(labels=Y_true, predictions=Y_pred).numpy())

results = model.evaluate(test_ds)
print(results)

acc = history.history['accuracy']
val_acc = history.history['val_accuracy']

loss = history.history['loss']
val_loss = history.history['val_loss']'''


## All of the above part works fine. The following part is intended for optimization of the model.