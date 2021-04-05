import numpy as np
import tensorflow as tf
from os.path import dirname, join


def pic_func(o, r, c):
    img_height = 224
    img_width = 224

    print(r, c)

    a = np.array(o)
    int.from_bytes(a, byteorder='big', signed=False)
    img = tf.convert_to_tensor(a, dtype_hint=np.int8)

    img = tf.reshape(img, shape=(r, c, 3))
    print(img)
    img = tf.cast(img, tf.uint8)
    var = tf.shape(img)
    print(var)

    print(img)

    filename = join(dirname(__file__), "my_model.h5")

    model = tf.keras.models.load_model(filename, compile = False)

    img_array = tf.expand_dims(img, 0)

    print(img)

    img_array = tf.cast(img_array, tf.float32)

    predictions = model.predict(img_array)
    score = tf.nn.softmax(predictions[0])

    class_names = 'benign', 'malignant'

    c_n = class_names[np.argmax(score)]
    confidence = 100 * np.max(score)
    return c_n, "{:.3f}".format(confidence)
