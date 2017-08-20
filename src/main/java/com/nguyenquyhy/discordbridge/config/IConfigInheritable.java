package com.nguyenquyhy.discordbridge.config;

/**
 * Created by Hy on 12/11/2016.
 */
public interface IConfigInheritable<T> {
    void inherit(T parent);
}
