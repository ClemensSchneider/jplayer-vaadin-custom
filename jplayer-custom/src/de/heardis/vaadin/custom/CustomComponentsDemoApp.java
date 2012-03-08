package de.heardis.vaadin.custom;

import java.util.Iterator;

import com.vaadin.Application;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.Transferable;
import com.vaadin.event.dd.DragAndDropEvent;
import com.vaadin.event.dd.DropHandler;
import com.vaadin.event.dd.DropTarget;
import com.vaadin.event.dd.TargetDetails;
import com.vaadin.event.dd.acceptcriteria.AcceptCriterion;
import com.vaadin.event.dd.acceptcriteria.Not;
import com.vaadin.event.dd.acceptcriteria.SourceIsTarget;
import com.vaadin.terminal.gwt.client.ui.dd.HorizontalDropLocation;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.DragAndDropWrapper;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.heardis.vaadin.custom.JPlayer.VolumeChangeEvent;
import de.heardis.vaadin.custom.JPlayer.VolumeChangeListener;

public class CustomComponentsDemoApp extends Application {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1168944442707744580L;

	@Override
	public void init() {
		Window mainWindow = new Window("Custom Components");
		mainWindow.setTheme("chameleon");
		Label label = new Label("Hello Vaadin user");
		final SortableLayout horizontalLayout = new SortableLayout(true);
//		final HorizontalLayout horizontalLayout = new HorizontalLayout();
		Button button = new Button("Button1");
		final JPlayer jPlayer = new JPlayer();
		
		final TextField textField = new TextField();
		
		button.addListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				jPlayer.setTrackTitle((String) textField.getValue());
				jPlayer.play();
				horizontalLayout.requestRepaintAll();
			}
		});
		horizontalLayout.addComponent(label);
		horizontalLayout.addComponent(jPlayer);
		horizontalLayout.addComponent(textField);
		horizontalLayout.addComponent(button);
		jPlayer.setMediaURL("http://www.archive.org/download/x_a_free_techno_compilation/01._x_compilation_-_new_delhi_fm_-_exit_peacemaker_www.derkleinegruenewuerfel.de.mp3");
//		jPlayer.setMediaURL("app://streaming/audio");
		jPlayer.setTrackTitle("Der kleine grüne Würfel");
		jPlayer.setVolume(0.8F);
		jPlayer.addListener(new VolumeChangeListener() {
			
			@Override
			public void volumeChange(VolumeChangeEvent event) {
				System.out.println(event.getVolume());
				textField.setValue("Got volume event - " + event.getVolume());
				
			}
		});
		
		DragAndDropWrapper horizontalDragAndDropWrapper = new DragAndDropWrapper(horizontalLayout);
		
		mainWindow.addComponent(horizontalDragAndDropWrapper);
		setMainWindow(mainWindow);
	}
	
	private static class SortableLayout extends CustomComponent {
        private final AbstractOrderedLayout layout;
        private final boolean horizontal;
        private DropHandler dropHandler;

        public SortableLayout(boolean horizontal) {
            this.horizontal = horizontal;
            if (horizontal) {
                layout = new HorizontalLayout();
                layout.setSpacing(true);
            } else {
                layout = new VerticalLayout();
            }
            dropHandler = new ReorderLayoutDropHandler(layout);

            DragAndDropWrapper pane = new DragAndDropWrapper(layout);
            setCompositionRoot(pane);
        }

        @Override
        public void addComponent(Component component) {
            WrappedComponent wrapper = new WrappedComponent(component,
                    dropHandler);
//            if (horizontal) {
//                component.setHeight("100%");
//                wrapper.setHeight("100%");
//            } else {
//                component.setWidth("100%");
//                wrapper.setWidth("100%");
//            }
            layout.addComponent(wrapper);
        }
    }
	
	private static class WrappedComponent extends DragAndDropWrapper {

        private final DropHandler dropHandler;

        public WrappedComponent(Component content, DropHandler dropHandler) {
            super(content);
            this.dropHandler = dropHandler;
            setDragStartMode(DragStartMode.WRAPPER);
        }

        @Override
        public DropHandler getDropHandler() {
            return dropHandler;
        }

    }
	
	private static class ReorderLayoutDropHandler implements DropHandler {

        private AbstractOrderedLayout layout;

        public ReorderLayoutDropHandler(AbstractOrderedLayout layout) {
            this.layout = layout;
        }

        public AcceptCriterion getAcceptCriterion() {
            return new Not(SourceIsTarget.get());
        }

        public void drop(DragAndDropEvent dropEvent) {
            Transferable transferable = dropEvent.getTransferable();
            Component sourceComponent = transferable.getSourceComponent();
            if (sourceComponent instanceof WrappedComponent) {
                TargetDetails dropTargetData = dropEvent.getTargetDetails();
                DropTarget target = dropTargetData.getTarget();

                // find the location where to move the dragged component
                boolean sourceWasAfterTarget = true;
                int index = 0;
                Iterator<Component> componentIterator = layout
                        .getComponentIterator();
                Component next = null;
                while (next != target && componentIterator.hasNext()) {
                    next = componentIterator.next();
                    if (next != sourceComponent) {
                        index++;
                    } else {
                        sourceWasAfterTarget = false;
                    }
                }
                if (next == null || next != target) {
                    // component not found - if dragging from another layout
                    return;
                }

                // drop on top of target?
                if (dropTargetData.getData("horizontalLocation").equals(
                        HorizontalDropLocation.CENTER.toString())) {
                    if (sourceWasAfterTarget) {
                        index--;
                    }
                }

                // drop before the target?
                else if (dropTargetData.getData("horizontalLocation").equals(
                        HorizontalDropLocation.LEFT.toString())) {
                    index--;
                    if (index < 0) {
                        index = 0;
                    }
                }

                // move component within the layout
                layout.removeComponent(sourceComponent);
                layout.addComponent(sourceComponent, index);
            }
        }
    };

}
