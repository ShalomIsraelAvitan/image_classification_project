package israela.image_classification_project;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style.AlignItems;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin.Horizontal;
@Route(value = "/measuremodel", layout = AppMainLayout.class)
public class MeasureModelPage extends VerticalLayout {

    public MeasureModelPage() {
        Image img = new Image("file:///C:/Users/user/Documents/VSProj/image_classification_project/src/main/resources/savePhotos/ConfusionMatrix.PNG", "Image");
        img.setHeight("250px");

        add(new H1("Measure Model Page"));
        add(new H3("Final Results:"));
        add(new H5("TP: 742"));
        add(new H5("FN: 167"));
        add(new H5("FP: 44"));
        add(new H5("TN: 856"));

        add(new H3("Precision: 0.9440203562340967"));
        add(new H3("Recall: 0.816282"));
        add(new H3("F1 score: 0.875516"));
        add(img);

        setAlignItems(Alignment.CENTER);
    }
}