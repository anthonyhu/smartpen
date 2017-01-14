# SmartPen

SmartPen, an undergraduate project at Telecom ParisTech, allows real-time communication whilst keeping the comfort of using a pen on a sheet of paper.

This project tried to overcome the tedious and imagination hampering communications using video conferences by letting people regain inspiration by actually using a pen.

## How it works

Everything you write on your sheet is automatically projected onto the sheets of the people you’re communicating with, and likewise you see what the others are writing. 

Pictures of what you’re writing are taken every second and compared in order to know when you’ve removed your hand from the sheet. When the latter happens, using RANSAC algorithm, the sheet is detected on the picture and transformed in order to be perfectly projected on the other people’s sheet.

## What did you do?

I was in charge of the hand detection and the image registration that was coded in Java.

## About the project

The SmartPen, which can be seen in the poster presenting the one-year project, finished first in the Telecom ParisTech Innovative Project and was subsequently shown to potential investors in the *2015 Innovation Day*.


