package israela.image_classification_project.Pages;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import israela.image_classification_project.AppMainLayout;
@Route(value = "/measuremodel", layout = AppMainLayout.class)
public class MeasureModelPage extends VerticalLayout {

    
    public MeasureModelPage() {
        
        HorizontalLayout heder = new HorizontalLayout();
        VerticalLayout vHeder = new VerticalLayout();
        Image img = new Image("images/ConfusionMatrix2.PNG","image");
        img.setHeight("250px");

        add(new H1("Measure Model Page"));

        vHeder.add(new H3("Precision: 0.8452868852459017"));
        vHeder.add(new H3("Recall: 0.894794"));
        vHeder.add(new H3("F1 score: 0.869336"));
        vHeder.setAlignItems(Alignment.BASELINE);
        
        heder.add(img,vHeder);
        add(heder);

        add(new H2("Model Structure"));
        Image img2 = new Image("images/ModelStructure2.PNG","ImageModelStructure");
        add(img2);

        setAlignItems(Alignment.CENTER);
    }
}