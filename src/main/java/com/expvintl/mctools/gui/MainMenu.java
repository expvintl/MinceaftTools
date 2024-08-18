package com.expvintl.mctools.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MainMenu extends Screen {
    public MainMenu(Text title) {
        super(title);
    }

    @Override
    protected void init() {

    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawText(textRenderer,"Test",100,100,-1,false);
        super.render(context, mouseX, mouseY, delta);
    }
}
