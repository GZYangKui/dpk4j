//
// Created by yangkui on 2021/10/9.
//
#include <stdio.h>
#include <gtk/gtk.h>

#include <X11/Xlib.h>
#include <X11/Xutil.h>
#include <string.h>
#include "../include/robot.h"

extern void mouse_drag(double x, double y) {
    Display *display = XOpenDisplay(NULL);
    if (display == NULL) {
        fprintf(stderr, "Error:can't create display instance!");
        exit(EXIT_FAILURE);
    }
    
//    XSetInputFocus(display,None,RectangleIn,CurrentTime);
//    XWarpPointer(display,None,None,0,0,0,0,x,y);
    XCloseDisplay(display);
}


