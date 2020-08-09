import os
from pathlib import Path

rootdir = '.'
extensions = ('.v159')

for path in Path(rootdir).rglob('*.v1*'):
	print(os.path.splitext(path.name)[0])
	path.rename(path.parent.joinpath( os.path.splitext(path.name)[0]))
#	os.rename(path.parent.dirname + path.name,path.parent.dirname + os.path.splitext(path.name)[0])
	ext = os.path.splitext(path.name)[-1].lower()
	if ext in extensions:
    		print(os.path.splitext(path.name)[0])
