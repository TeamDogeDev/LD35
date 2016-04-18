package de.dogedev.ld35.ashley;

import com.badlogic.ashley.core.ComponentMapper;
import de.dogedev.ld35.ashley.components.*;

/**
 * Created by Furuha on 12.03.2016.
 */
public class ComponentMappers {

    public static final ComponentMapper<PositionComponent> position = ComponentMapper.getFor(PositionComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<SpriteComponent> sprite = ComponentMapper.getFor(SpriteComponent.class);
    public static final ComponentMapper<AnimationComponent> animation = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<AccelerationComponent> acceleration = ComponentMapper.getFor(AccelerationComponent.class);
    public static final ComponentMapper<NameComponent> name = ComponentMapper.getFor(NameComponent.class);
    public static final ComponentMapper<SizeComponent> size = ComponentMapper.getFor(SizeComponent.class);
    public static final ComponentMapper<LightComponent> light = ComponentMapper.getFor(LightComponent.class);
    public static final ComponentMapper<TextboxComponent> textbox = ComponentMapper.getFor(TextboxComponent.class);
    public static final ComponentMapper<PlayerComponent> player = ComponentMapper.getFor(PlayerComponent.class);
    public static final ComponentMapper<KeyComponent> key = ComponentMapper.getFor(KeyComponent.class);
    public static final ComponentMapper<GravityComponent> gravity = ComponentMapper.getFor(GravityComponent.class);
}
