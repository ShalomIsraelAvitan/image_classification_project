package israela.image_classification_project;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
@Route(value = "/measuremodel", layout = AppMainLayout.class)
public class MeasureModelPage extends VerticalLayout {

    
    public MeasureModelPage() {
        
        HorizontalLayout heder = new HorizontalLayout();
        VerticalLayout vHeder = new VerticalLayout();
        Image img = new Image("images/ConfusionMatrix.PNG","image");
        img.setHeight("250px");

        add(new H1("Measure Model Page"));
        // add(new H3("Final Results:"));
        // add(new H5("TP: 742"));
        // add(new H5("FN: 167"));
        // add(new H5("FP: 44"));
        // add(new H5("TN: 856"));

        vHeder.add(new H3("Precision: 0.9440203562340967"));
        vHeder.add(new H3("Recall: 0.816282"));
        vHeder.add(new H3("F1 score: 0.875516"));
        vHeder.setAlignItems(Alignment.BASELINE);
        
        heder.add(img,vHeder);
        add(heder);

        add(new H2("Model Structure"));
        Image img2 = new Image("images/ModelStructure.PNG","ImageModelStructure");
        add(img2);

        setAlignItems(Alignment.CENTER);
    }
}