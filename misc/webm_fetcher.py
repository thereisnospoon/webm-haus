from urllib import request
from urllib import parse
import re

output_folder = 'C:\\Users\\nagrizolich\\Desktop\\webms\\'
url = "https://2ch.hk/b/res/91727737.html"
parsed_url = parse.urlparse(url)
host = parsed_url.scheme + '://' + parsed_url.netloc

print('Host is: {}'.format(host))

response = request.urlopen(url).read()
matchers = re.finditer('href="([/\w]+\.webm)"', str(response), re.MULTILINE)
urls = {matcher.group(1) for matcher in matchers}
number_of_webms = len(urls)

for i, url in enumerate(urls):
	print("Fetching {}{}. ({} of {})".format(host, url, i + 1, number_of_webms))
	response = request.urlopen('{}{}'.format(host, url))
	file_name = re.search('/(\w+.webm$)', url).group(1)
	file = open(output_folder + file_name, 'wb')
	file.write(response.read())
	file.close()