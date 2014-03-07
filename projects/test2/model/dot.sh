#!/bin/sh

dot -Tpdf acg.dot -o acg.pdf
dot -Tpng acg.dot -o acg.png
dot -Tpdf xcg.dot -o xcg.pdf
dot -Tpng xcg.dot -o xcg.png

dot -Tpng fsm.dot -o fsm.png
dot -Tpdf fsm.dot -o fsm.pdf



