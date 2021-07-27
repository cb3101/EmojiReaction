package harmonyemojireactionview;

/*
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
 */

import com.example.emoji_reaction.ResourceTable;
import com.ritik.emojireactionlibrary.ClickInterface;
import com.ritik.emojireactionlibrary.EmojiReactionView;
import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.window.dialog.ToastDialog;
import ohos.global.resource.NotExistException;
import ohos.global.resource.WrongTypeException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SimpleExample extends Fraction {

    EmojiReactionView myImage;
    int clickedEmoji = 0;

    public SimpleExample() {
        // Required empty public constructor
    }


    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
    }

    /*
    @Override
    public Component onComponentAttached(LayoutScatter scatter,
                                   ComponentContainer container,
                                   Intent intent)
     */
    public Component onCreateView(@NotNull LayoutScatter inflater, ComponentContainer container)
            throws NotExistException, WrongTypeException, IOException {
        // Inflate the layout for this fragment
        Component component = inflater.parse(ResourceTable.Layout_simple, container, false);
        myImage = (EmojiReactionView) component.findComponentById(ResourceTable.Id_image);


        myImage.setOnEmojiClickListener(new ClickInterface() {
            @Override
            public void onEmojiClicked(int emojiIndex, int x, int y) {
                String message;
                if (x != -1) {
                    switch (emojiIndex) {
                        case 0:
                            message = " Great!! ";
                            break;
                        case 1:
                            message = " Hehe ";
                            break;
                        case 2:
                            message = " Loved... ";
                            break;
                        case 3:
                            message = " Shocked!! ";
                            break;
                        case 4:
                            message = " Sad... ";
                            break;
                        case 5:
                            message = " Lit!! ";
                            break;
                        default:
                            message = " ** ";
                    }
                    ToastDialog toastDialog = new ToastDialog(getContext());
                    toastDialog.setText(message).show();
                }
                clickedEmoji = emojiIndex;
            }

            @Override
            public void onEmojiUnclicked(int emojiIndex, int x, int y) {

            }

        });
        return component;
    }
}

