#loaded_model = keras.models.load_model("C:\\Users\\user\\Desktop\\models")
import tensorflow as tf
from tensorflow import keras
import sys
import numpy as np
from py4j.java_gateway import JavaGateway, GatewayParameters
#print("Enter to CNN\n")

def predict_image(loaded_model, image_path):
        
        img = tf.keras.utils.load_img(image_path,target_size=(256,256))
        img_array = tf.keras.utils.img_to_array(img)
        img_array = tf.expand_dims(img_array, 0) # Create a batch

        newImg = loaded_model.predict(img_array)
        #print("newImg[0][0] = {}".format(newImg[0][0]))
        #print("newImg[0] = {}".format(newImg[0]))
        #print("newImg = {}".format(newImg))
        predictions = tf.nn.softmax(newImg[0])
        #predictions = loaded_model.predict(preprocessed_image)

        # Return the predictions or perform further processing as needed
        #return predictions.tolist()  # Convert predictions to a list for Java compatibility
        #print("predictions = "+predictions.tolist())
        print("predictions = {}".format(predictions[1].numpy()*100))
        print(predictions[1].numpy()*100)
        return predictions[1]

#preprocessed_image = "C:\\Users\\user\\Desktop\\models"

loaded_model = keras.models.load_model("C:\\Users\\user\\Desktop\\models")
#C:\Users\user\Documents\VSProj\milestone2\src\main\resources\model
#loaded_model = keras.models.load_model("src\\main\\resources\\model")
image_path = sys.argv[1]
#print("phyton image_path= "+image_path+"\n")
pre = predict_image(loaded_model,image_path)
#print(pre)
        

'''
# Example usage:
model_path = "path/to/save/model.h5"  # Replace with the actual path to your saved model
cnn_model = CNN(model_path)

# Example prediction on a new image
image_path = "path/to/new/image.jpg"  # Replace with the actual path to your new image
predictions = cnn_model.predict_image(image_path)

# Use predictions as needed
print(predictions)

'''

'''
class CNN:
    def __init__(self, model_path):
        self.model_path = model_path
        self.model = None

    def load_model(self):
        self.model = keras.models.load_model(model_path)

    def predict_image(self, image_path):
        # Make predictions using the loaded model
        # ...
        img = tf.keras.utils.load_img('Abstract_image_2307.jpg',target_size=(256,256))
        img_array = tf.keras.utils.img_to_array(img)
        img_array = tf.expand_dims(img_array, 0) # Create a batch

        predictions = self.model.predict(preprocessed_image)

        # Return the predictions or perform further processing as needed
        return predictions.tolist()  # Convert predictions to a list for Java compatibility


'''

# Start Py4J gateway
'''
gateway = JavaGateway(gateway_parameters=GatewayParameters(port=25555))

# Create an instance of the CNN class
cnn_model = CNN(model_path="path/to/save/model.h5")

# Expose the CNN instance to Java
gateway.java_gateway_server.get_gateway().get_server().register_object("cnnModel", cnn_model)

# Keep the Python process running
input("Press Enter to terminate.")
'''
