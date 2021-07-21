package harmonyemojireactionview;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.emoji_reaction.ResourceTable;
import harmonyemojireactionview.slice.MainAbilitySlice;
import emojireaction.ClickInterface;
import emojireaction.EmojiReactionView;
import ResourceTable


import ohos.aafwk.ability.fraction.Fraction;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.agp.components.LayoutScatter;
import ohos.agp.components.TabList;
import ohos.agp.render.layoutboost.LayoutBoost;
import ohos.agp.text.Layout;
import ohos.agp.window.dialog.ToastDialog;
import ohos.bundle.BundleInfo;
import ohos.bundle.IBundleManager;
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

    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

     */

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
    }

    @Override
    public Component onCreateView(@NotNull LayoutScatter inflater, ComponentContainer container, Intent intent
                                  // Bundle savedInstanceState
                                ) throws NotExistException, WrongTypeException, IOException {
        // Inflate the layout for this fragment
        Component component = inflater.parse(1300007 /*R.layout.simple*/, container, false);
        myImage = component.findComponentById(ResourceTable.Id_image);
        if (savedInstanceState != null) {
            clickedEmoji = savedInstanceState.getInt("emojiNumber");
            myImage.setClickedEmojiNumber(clickedEmoji);
        }

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




    /*
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.simple, container, false);
        myImage = view.findViewById(R.id.image);
        if (savedInstanceState != null) {
            clickedEmoji = savedInstanceState.getInt("emojiNumber");
            myImage.setClickedEmojiNumber(clickedEmoji);
        }
     */
//
//        myImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "clicked!!", Toast.LENGTH_SHORT).show();
//            }
//        });

    public Component onCreateView(@NotNull LayoutScatter inflater, ComponentContainer container,
                                  Bundle savedInstanceState) throws NotExistException, WrongTypeException, IOException {
        // Inflate the layout for this fragment
        Component component = inflater.parse(ResourceTable.layout.simple, container, false);

        myImage = component.findComponentById(ResourceTable.id.image);
        if (savedInstanceState != null) {
            clickedEmoji = savedInstanceState.getInt("emojiNumber");
            myImage.setClickedEmojiNumber(clickedEmoji);
        }
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
//                if (x != -1)
//                    Toast.makeText(getActivity(), "Emoji " + emojiIndex +" removed", Toast.LENGTH_SHORT).show();
            }
        });
        return component;

    }

    @Override
    public void onSaveInstanceState(@NotNull Bundle outState) {
        outState.putInt("emojiNumber", clickedEmoji);
        super.onSaveInstanceState(outState);
    }

}

